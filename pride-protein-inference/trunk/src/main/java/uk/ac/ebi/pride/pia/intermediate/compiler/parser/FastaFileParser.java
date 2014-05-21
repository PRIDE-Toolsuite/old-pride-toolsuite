package uk.ac.ebi.pride.pia.intermediate.compiler.parser;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;


import uk.ac.ebi.pride.data.core.CVLookup;
import uk.ac.ebi.pride.pia.intermediate.Accession;
import uk.ac.ebi.pride.pia.intermediate.AccessionOccurrence;
import uk.ac.ebi.pride.pia.intermediate.PIAInputFile;
import uk.ac.ebi.pride.pia.intermediate.Peptide;
import uk.ac.ebi.pride.pia.intermediate.PeptideSpectrumMatch;
import uk.ac.ebi.pride.pia.intermediate.compiler.PIACompiler;
import uk.ac.ebi.pride.pia.modeller.score.ScoreModel;
import uk.ac.ebi.pride.pia.modeller.score.ScoreModelEnum;

public class FastaFileParser {
	
	/**
	private static final Logger logger = Logger.getLogger(FastaFileParser.class);
	

	private FastaFileParser() {
		throw new AssertionError();
	}
	
	

	public static boolean getDataFromFastaFile(String name, String fileName,
			PIACompiler compiler, String enzymePattern, int minPepLength,
			int maxPepLength, int missedCleavages) {
		if (missedCleavages < 0) {
			logger.warn("You allowed for all possible missed cleavages, this " +
					"may result in a massive file and take very long!");
		}
		
		try {
			FileInputStream fileStream = new FileInputStream(fileName);
			
			DataInputStream in = new DataInputStream(fileStream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			CVLookup psiMS = new CVLookup("PSI-MS","PSI-MS", "","http://psidev.cvs.sourceforge.net/viewvc/*checkoutpsidev/psi/psi-ms/mzML/controlledVocabulary/psi-ms.obo");

			PIAInputFile inputFile =
					compiler.insertNewFile(name, fileName, "FASTA");
			
			// add the searchDB (actually, this FASTA file)
			SearchDatabase searchDatabase = new SearchDatabase();
			searchDatabase.setId("fastaFile");
			searchDatabase.setLocation(fileName);
			
			FileFormat fileFormat = new FileFormat();
			AbstractParam abstractParam = new CvParam();
			((CvParam)abstractParam).setAccession("MS:1001348");
			((CvParam)abstractParam).setCv(psiMS);
			abstractParam.setName("FASTA format");
			fileFormat.setCvParam((CvParam)abstractParam);
			searchDatabase.setFileFormat(fileFormat);
			
			searchDatabase = compiler.putIntoSearchDatabasesMap(searchDatabase);
			
			// add the spectrumIdentificationProtocol
			SpectrumIdentificationProtocol spectrumIDProtocol =
					new SpectrumIdentificationProtocol();
			spectrumIDProtocol.setId("fastaParsing");
			// TODO: set this: spectrumIDProtocol.setAnalysisSoftware(PIA);
			inputFile.addSpectrumIdentificationProtocol(spectrumIDProtocol);
			
			// add the spectrum identification
			SpectrumIdentification spectrumID = new SpectrumIdentification();
			spectrumID.setId("fastaParsing");
			spectrumID.setSpectrumIdentificationList(null);
			spectrumID.setSpectrumIdentificationProtocol(spectrumIDProtocol);
			
			SearchDatabaseRef searchDBRef = new SearchDatabaseRef();
			searchDBRef.setSearchDatabase(searchDatabase);
			spectrumID.getSearchDatabaseRef().add(searchDBRef);
			
			inputFile.addSpectrumIdentification(spectrumID);
			
			
			String strLine;
			int spectrumOffset = 0;
			int accessions = 0;
			FastaHeaderInfos headerInfos = null;
			StringBuffer dbSequenceBuffer = null;
			
			while ((strLine = br.readLine()) != null) {
				if (strLine.startsWith(">")) {
					if ((headerInfos != null) && (dbSequenceBuffer != null) &&
							(dbSequenceBuffer.length() > 0)) {
						// a prior protein can be inserted and digested
						spectrumOffset += digestProtein(headerInfos,
								dbSequenceBuffer.toString(),
								compiler,
								inputFile,
								spectrumID,
								searchDatabase.getId(),
								enzymePattern,
								minPepLength,
								maxPepLength,
								missedCleavages,
								spectrumOffset);
						accessions++;
						
						if (accessions % 100000 == 0) {
							logger.info(accessions + " accessions processed");
						}
					}
					
					// start of a new protein
					headerInfos = FastaHeaderInfos.parseHeaderInfos(strLine);
					dbSequenceBuffer = new StringBuffer();
				} else {
					// just reading in the protein sequence
					dbSequenceBuffer.append(strLine.trim());
				}
			}
			
			if ((headerInfos != null) && (dbSequenceBuffer != null) &&
					(dbSequenceBuffer.length() > 0)) {
				// the last protein can be inserted and digested
				spectrumOffset += digestProtein(headerInfos,
						dbSequenceBuffer.toString(),
						compiler,
						inputFile,
						spectrumID,
						searchDatabase.getId(),
						enzymePattern,
						minPepLength,
						maxPepLength,
						missedCleavages,
						spectrumOffset);
			}
			
			in.close();
		} catch (Exception e) {
			logger.error("Error while parsing the FASTA file", e);
		}
		
		return true;
	}
	
	

	private static int digestProtein(FastaHeaderInfos fastaHeader,
			String dbSequence, PIACompiler compiler, PIAInputFile inputFile,
			SpectrumIdentification spectrumID, String searchDBRef,
			String enzymePattern, int minPepLength, int maxPepLength,
			int missedCleavages, int spectrumCountOffset) {
		Accession accession;
		
		// first, look if the accession is already in the compilation (this should not be the case!)
		accession = compiler.getAccession(fastaHeader.getAccession());
		if (accession != null) {
			logger.warn("Protein with accession " + accession.getAccession() +
					" already in the compilation! Only keeping the sequence " +
					"of the first accession.");
			return 0;
		}
		
		// put the new accession into the compiler
		accession = compiler.insertNewAccession(fastaHeader.getAccession(),
				dbSequence);
		
		accession.addFile(inputFile.getID());
		
		accession.addDescription(inputFile.getID(),
				fastaHeader.getDescription());
		
		accession.addSearchDatabaseRef(searchDBRef);;
		
		// digest the protein
		String peptides[] = dbSequence.split(enzymePattern);
		
		// if the missedCleavages is below 0, allow for all possible missed cleavages
		if (missedCleavages < 0) {
			missedCleavages = peptides.length - 1;
		}
		
		// get the peptides
		int spectra_count = 1;
		for (int missed=0; missed <= missedCleavages; missed++) {
			
			int start = 0;
			for (int i = 0; i < peptides.length-missed; i++) {
				// build the sequence with misses
				StringBuffer sequence = new StringBuffer(peptides[i]);
				for (int miss=1; miss <= missed; miss++) {
					sequence.append(peptides[i+miss]);
				}
				
				if ((sequence.length() >= minPepLength) &&
						(sequence.length() <= maxPepLength)) {
					addSequence(sequence.toString(),
							accession,
							start+1,
							missed,
							compiler,
							inputFile,
							spectrumID,
							spectrumCountOffset+spectra_count);
				}
				
				start += peptides[i].length();
				spectra_count++;
			}
			
		}
		
		return spectra_count;
	}
	
	

	private static void addSequence(String sequence, Accession accession,
			int start, int missed, PIACompiler compiler, PIAInputFile inputFile,
			SpectrumIdentification spectrumID, int spectrumCount) {
		Peptide peptide = compiler.getPeptide(sequence);
		
		if (peptide == null) {
			peptide = compiler.insertNewPeptide(sequence);
			
			// only add one PSM for one peptide-sequence
			// TODO: calculate the mass
			double massToCharge = sequence.length();
			
			String sourceID = "index=" + spectrumCount;
			String spectrumTitle = sequence;
			
			PeptideSpectrumMatch psm;
			psm = compiler.insertNewSpectrum(
					2,					// just a pseudo-charge
					massToCharge,
					0,
					null,
					sequence,
					missed,
					sourceID,
					spectrumTitle,
					inputFile,
					spectrumID);
			
			peptide.addSpectrum(psm);
			
			// add the "FASTA Sequence Count" score
			ScoreModel score = new ScoreModel(1.0,
					ScoreModelEnum.FASTA_SEQUENCE_COUNT);
			psm.addScore(score);
			
			// add the "FASTA Accession Count" score
			score = new ScoreModel(0.0,
					ScoreModelEnum.FASTA_ACCESSION_COUNT);
			psm.addScore(score);
		} else {
			// increase the "FASTA Sequence Count" score
			for (PeptideSpectrumMatch psm : peptide.getSpectra()) {
				ScoreModel score = 
						psm.getScore(ScoreModelEnum.FASTA_SEQUENCE_COUNT.getShortName());
				
				Double value = score.getValue();
				score.setValue(value + 1);
				break;
			}
		}
		
		boolean increaseAccessionCount = true;
		for (AccessionOccurrence occ : peptide.getAccessionOccurrences()) {
			// only count the accessions once for the "FASTA Accession Count"
			if (accession.getID().equals(occ.getAccession().getID())) {
				increaseAccessionCount = false;
				break;
			}
		}
		if (increaseAccessionCount) {
			// increase the "FASTA Accession Count" score
			for (PeptideSpectrumMatch psm : peptide.getSpectra()) {
				ScoreModel score = 
						psm.getScore(ScoreModelEnum.FASTA_ACCESSION_COUNT.getShortName());
				
				Double value = score.getValue();
				score.setValue(value + 1);
				break;
			}
		}
		
		peptide.addAccessionOccurrence(accession,
				start,
				start+sequence.length()-1);
		
		// now insert the peptide and the accession into the accession peptide map
		Set<Peptide> accsPeptides =
				compiler.getFromAccPepMap(accession.getAccession());
		if (accsPeptides == null) {
			accsPeptides = new HashSet<Peptide>();
			compiler.putIntoAccPepMap(accession.getAccession(), accsPeptides);
		}
		accsPeptides.add(peptide);
		
		// and also insert them into the peptide accession map
		Set<Accession> pepsAccessions =
				compiler.getFromPepAccMap(peptide.getSequence());
		if (pepsAccessions == null) {
			pepsAccessions = new HashSet<Accession>();
			compiler.putIntoPepAccMap(peptide.getSequence(),
					pepsAccessions);
		}
		pepsAccessions.add(accession);
	}
	
	
	
	public static void main(String[] args) {
		
		// filename missed minPepLength maxPepLength enzymePattern outfile
		
		if (args.length > 5) {
			PIACompiler piaCompiler = new PIACompiler();
			
			String fileName = args[0];
			String name = (new File(fileName).getName());
			
			Integer missedCleavages = Integer.parseInt(args[1]);
			
			Integer minPepLength = Integer.parseInt(args[2]);
			Integer maxPepLength = Integer.parseInt(args[3]);
			
			String enzymePattern = args[4];
			
			String outFile = args[5];
			
			getDataFromFastaFile(name,
					fileName,
					piaCompiler,
					enzymePattern,
					minPepLength,
					maxPepLength,
					missedCleavages);
			
			piaCompiler.buildClusterList();
			
			piaCompiler.buildIntermediateStructure();
			
			piaCompiler.setName(name);
			piaCompiler.writeOutXML(outFile);
		} else {
			System.out.println("usage: "+PIACompiler.class.getName()+
					" fileName missed minPepLength maxPepLength enzymePattern outFile");
		}
	}*/

}
