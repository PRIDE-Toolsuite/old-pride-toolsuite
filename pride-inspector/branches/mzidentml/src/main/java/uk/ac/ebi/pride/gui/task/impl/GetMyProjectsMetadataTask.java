package uk.ac.ebi.pride.gui.task.impl;

import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import uk.ac.ebi.pride.archive.web.service.model.project.ProjectDetailList;
import uk.ac.ebi.pride.gui.PrideInspector;
import uk.ac.ebi.pride.gui.desktop.DesktopContext;
import uk.ac.ebi.pride.gui.task.Task;

import java.util.Arrays;

/**
 * GetPrideUserDetailTask retrieves pride user details using pride web service
 *
 * @author Rui Wang
 * @version $Id$
 */
public class GetMyProjectsMetadataTask extends Task<ProjectDetailList, String> {

    private RestTemplate restTemplate;

    private HttpEntity<String> requestEntity;

    /**
     * Constructor
     *
     * @param userName pride user name
     * @param password pride password
     */
    public GetMyProjectsMetadataTask(String userName, char[] password) {
        final HttpHeaders headers = getHeaders(userName, password);
        this.requestEntity = new HttpEntity<String>(headers);
        this.restTemplate = new RestTemplate();
    }

    @Override
    protected ProjectDetailList doInBackground() throws Exception {
        DesktopContext context = PrideInspector.getInstance().getDesktopContext();
        String projectMetadataUrl = context.getProperty("prider.my.projects.metadata.url");

        try {
            ResponseEntity<ProjectDetailList> entity = restTemplate.exchange(projectMetadataUrl, HttpMethod.GET, requestEntity, ProjectDetailList.class);
            return entity.getBody();
        } catch (RestClientException ex) {
            publish("Failed to login, please check your username and password");
            return null;
        }
    }

    private HttpHeaders getHeaders(String userName, char[] password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        if (userName != null && password != null) {
            String auth = userName + ":" + new String(password);
            byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
            headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
        }

        return headers;
    }

    @Override
    protected void finished() {
    }

    @Override
    protected void succeed(ProjectDetailList results) {
    }

    @Override
    protected void cancelled() {
    }

    @Override
    protected void interrupted(InterruptedException iex) {
    }
}

