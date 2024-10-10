package de.algoviz.algoviz.database;

import de.algoviz.algoviz.controllers.graph.GraphTemplateController;
import de.algoviz.algoviz.database.datamodel.StoredGraph;
import de.algoviz.algoviz.database.repository.StoredGraphRepository;
import de.algoviz.algoviz.external.graph.GraphExternal;
import de.algoviz.algoviz.parser.ParseException;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class StoredGraphTest {

    private static final String TEST_GRAPH = """
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
    public static final String FILE_NAME = "binary_tree.dot";
    public static final String GRAPH_NAME = "Test Object";

    @Autowired
    private DotFormatParser dotFormatParser;

    @Test
    void getGraphTemplateTest() throws ParseException {
        StoredGraphRepository storedGraphRepository = Mockito.mock(StoredGraphRepository.class);
        GraphTemplateController graphTemplateController = new GraphTemplateController(storedGraphRepository, dotFormatParser);
        List<Long> idLIst = new ArrayList<>();
        idLIst.add(1L);

        Mockito.when(storedGraphRepository.findAllIds()).thenReturn(idLIst);

        long graphId = graphTemplateController.getTemplatedGraphIds().get(0);

        StoredGraph storedGraph = new StoredGraph(graphId, FILE_NAME, "", GRAPH_NAME);
        Mockito.when(storedGraphRepository.findById(graphId)).thenReturn(Optional.of(storedGraph));

        Assertions.assertEquals(graphTemplateController.getTemplateGraph(graphId), new GraphExternal(dotFormatParser.parse(TEST_GRAPH)));



    }
}
