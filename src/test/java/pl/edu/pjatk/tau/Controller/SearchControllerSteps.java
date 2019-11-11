package pl.edu.pjatk.tau.Controller;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

import junit.framework.Assert;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import org.jbehave.core.model.ExamplesTable;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import pl.edu.pjatk.tau.Domain.PentestingSession;
import pl.edu.pjatk.tau.Service.IOTimesService;
import pl.edu.pjatk.tau.Service.PentestingSessionService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchControllerSteps {
    private SearchController searchController;
    private String regexp;


    @Given("a search functionality with initial data: $table")
    public void searchControllerSetup(ExamplesTable table){
        PentestingSessionService sessionsService = new PentestingSessionService(
                toPentestSessionsHashMap(table),
                new IOTimesService()
        );

        searchController = new SearchController(sessionsService);
    }

    @When("using regexp in search like $regexp")
    public void setPhrase(String phrase){
        regexp = phrase;
    }

    @Then("it should return a list: $results")
    public void shouldReturnSearch(ExamplesTable results){
        List<PentestingSession> expected = toPentestSessionsList(results);
        List<PentestingSession> actual = searchController.searchByRegexp(regexp);

        assertTrue(new ReflectionEquals(expected).matches(actual));
    }

    private HashMap<Integer, PentestingSession> toPentestSessionsHashMap(ExamplesTable table) {
        HashMap<Integer, PentestingSession> sessions = new HashMap<>();
        int position = 0;

        for (Map<String,String> row : table.getRows()) {
            String title = row.get("title");
            String riskType = row.get("riskType");
            int time = Integer.parseInt(row.get("time"));
            float weight = Float.parseFloat(row.get("weight"));

            PentestingSession session = new PentestingSession(title);
            session.setRiskType(riskType);
            session.setTimeInMinutes(time);
            session.setWeight(weight);

            sessions.put(position++, session);
        }

        return sessions;
    }

    private List<PentestingSession> toPentestSessionsList(ExamplesTable table) {
        List<PentestingSession> sessions = new ArrayList<>();

        for (Map<String,String> row : table.getRows()) {
            String title = row.get("title");
            String riskType = row.get("riskType");
            int time = Integer.parseInt(row.get("time"));
            float weight = Float.parseFloat(row.get("weight"));

            PentestingSession session = new PentestingSession(title);
            session.setRiskType(riskType);
            session.setTimeInMinutes(time);
            session.setWeight(weight);

            sessions.add(session);
        }

        return sessions;
    }
}
