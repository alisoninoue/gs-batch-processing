package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestPersonReader implements ItemReader<Person> {

    private static final Logger log = LoggerFactory.getLogger(RestPersonReader.class);
    private final String apiUrl;
    private final RestTemplate restTemplate;

    private int nextPersonIndex;

    public RestPersonReader(String apiUrl, RestTemplate restTemplate) {
        this.apiUrl = apiUrl;
        this.restTemplate = restTemplate;
        this.nextPersonIndex = 0;
    }

    @Override
    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        Person person = callAPI();
        if (nextPersonIndex > 1) {
            return null;
        }
        log.info("Retorno da API: " + person);
        return person;
    }

    private Person callAPI() {
        ResponseEntity<Person> response = restTemplate.getForEntity(
                apiUrl,
                Person.class
        );
        nextPersonIndex++;
        return response.getBody();
    }
}
