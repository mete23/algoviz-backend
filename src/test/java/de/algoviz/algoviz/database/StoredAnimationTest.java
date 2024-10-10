package de.algoviz.algoviz.database;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.algoviz.algoviz.controllers.StoredAnimationController;
import de.algoviz.algoviz.database.datamodel.StoredAnimation;
import de.algoviz.algoviz.database.repository.StoredAnimationRepository;
import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.model.ApplicationData;
import de.algoviz.algoviz.model.algorithm.AlgorithmGenerator;
import de.algoviz.algoviz.model.algorithm.AlgorithmManager;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.session.UserSession;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import de.algoviz.algoviz.util.data_file_manager.DataFileManager;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.testng.annotations.BeforeMethod;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.notNull;

@SpringBootTest
public class StoredAnimationTest {

    private static final int SESSION_ID = 1;
    private static final String GRAPH_FILE_PATH = "stored_animations/";
    private static final String GRAPH_FILE_NAME = "test_animation.json";
    private static final StoredAnimation DUMMY_STORED_ANIMATION = new StoredAnimation(GRAPH_FILE_NAME, AlgorithmGenerator.BFS);
    private static Graph dummyGraph;
    private static String TEST_GRAPH_JSON;
    private static final String TEST_GRAPH_DOT = """
            graph G{
            1[pos="0.50000,1.00000!"]
            2[pos="0.25000,0.60000!"]
            3[pos="0.75000,0.60000!"]
            4[pos="0.12500,0.20000!"]
            5[pos="0.37500,0.20000!"]
            6[pos="0.62500,0.20000!"]
            7[pos="0.87500,0.20000!"]
            1--2 [weight=1]
            1--3 [weight=1]
            2--4 [weight=3]
            2--5 [weight=2]
            3--6 [weight=4]
            3--7 [weight=3]
            }
            """;


    @Autowired
    StoredAnimationController storedAnimationController;

    @MockBean
    private ApplicationData applicationData;
    @MockBean
    private StoredAnimationRepository storedAnimationRepository;


    @BeforeMethod
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @BeforeAll
    static void setup() throws ParseException, JsonProcessingException {
        DotFormatParser parser = new DotFormatParser();
        ObjectMapper mapper = new ObjectMapper();
        dummyGraph = parser.parse(TEST_GRAPH_DOT);
        TEST_GRAPH_JSON = mapper.writeValueAsString(new GraphExternal(dummyGraph));
        DUMMY_STORED_ANIMATION.setId(1L);
    }

    @AfterEach
    void tearDown() throws FileNotFoundException {
        DataFileManager.removeData(GRAPH_FILE_PATH, GRAPH_FILE_NAME);
    }

    @Test
    void storeAnimationTest() throws IOException {
        //init algorithmManager
        AlgorithmManager algorithmManager = Mockito.mock(AlgorithmManager.class);
        Mockito.when(algorithmManager.getGenerator()).thenReturn(AlgorithmGenerator.BFS);

        //init session
        UserSession session = Mockito.mock(UserSession.class);
        Mockito.when(session.getGraph()).thenReturn(dummyGraph);
        Mockito.when(session.getAlgorithmManager()).thenReturn(algorithmManager);

        //mock database(storedAnimationRepository) and applicationData
        Mockito.when(applicationData.getSession(SESSION_ID)).thenReturn(session);
        Mockito.when(storedAnimationRepository.save(notNull())).thenReturn(DUMMY_STORED_ANIMATION);

        storedAnimationController.storeAnimation(SESSION_ID);

        Assertions.assertEquals(TEST_GRAPH_JSON, DataFileManager.getData(GRAPH_FILE_PATH, GRAPH_FILE_NAME).replace("\n", ""));
    }

    @Test
    void setAnimationTest() throws IOException {
        //initialisation
        DataFileManager.insertData(GRAPH_FILE_PATH, GRAPH_FILE_NAME, TEST_GRAPH_JSON);
        UserSession session = Mockito.spy(UserSession.class);

        //mock database(storedAnimationRepository) and applicationData
        Mockito.when(storedAnimationRepository.findById(1L)).thenReturn(java.util.Optional.of(DUMMY_STORED_ANIMATION));
        Mockito.when(applicationData.getSession(SESSION_ID)).thenReturn(session);

        storedAnimationController.setAnimation(1L, SESSION_ID);

        Assertions.assertEquals(session.getGraph(), dummyGraph);
        Assertions.assertEquals(session.getAlgorithmManager().getGenerator(), AlgorithmGenerator.BFS);
    }
}
