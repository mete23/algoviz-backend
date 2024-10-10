package de.algoviz.algoviz.parser;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.model.log_file.LogFile;
import de.algoviz.algoviz.model.session.UserSession;
import de.algoviz.algoviz.parser.logfile_parser.LogFileParser;
import de.algoviz.algoviz.util.external.ColorConverter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class LogFileParserTest {
    private static final String GRAPH = """
            graph G{
            1[pos="0.00000,0.00000!"]
            2[pos="1.00000,0.00000!"]
            3[pos="1.00000,1.00000!"]
            4[pos="1.00000,1.00000!"]
            1--2
            2--3
            }
            """;
    private static final String ALGORITHM = "node:1:label:\"test\"";

    private static Stream<Arguments> generateIllegalFiles() {
        String graph = GRAPH + "\n###\n";
        String algorithm = ALGORITHM + "\n";

        return Stream.of(
                "",
                "test",
                "graphG{\n}", // algorithm missing
                "graphG{\ntest\n}\n###", // test illegal graph
                "graphG{\ntest\n}\n###", // test illegal graph

                graph + "test", // test unknown command

                GRAPH, // test missing algorithm
                GRAPH + algorithm, // test missing line separator
                GRAPH + "#" + algorithm, // test illegal line separator too less #
                GRAPH + "##" + algorithm, // test illegal line separator too less #
                GRAPH + "####" + algorithm, // test illegal line separator too much #
                GRAPH + "###;" + algorithm, // test illegal character ;
                GRAPH + "###:" + algorithm, // test illegal character :
                GRAPH + "#\n#\n#" + algorithm, // test splitting line separator


                // node coordinates
                graph + "node:1:coordinate:0.5:0.5", // test misspelled command
                graph + "node:1:coordinates:0.5:0.5:0.5", // test too much coordinates
                graph + "node:1:coordinates:0.5", // test too less coordinates
                graph + "node:1:coordinates:0.5:", // test illegal coordinates
                graph + "node:1:coordinates:0.5:0,5", // test illegal character ,
                graph + "node:1:coordinates:0.5:0.5;", // test illegal character ;
                graph + "node:1;coordinates:0.5:0.5", // test illegal character ;
                graph + "node:1:coordinates:0.5::0.5", // test too much :
                graph + "node:1:coordinates:0.5:0.5:", // test too much :
                graph + "node:1:coordinates::0.5:0.5", // test too much :
                graph + "node:1::coordinates:0.5:0.5:0.5", // test too much :
                graph + "node:1:coordinates:-1:0", // test illegal negative coordinates
                graph + "node:1:coordinates:0:1.5", // test illegal coordinates out of range
                graph + "node:1:coordinates:0.5:1.001", // test illegal coordinates out of range
                graph + "node:1.000:c:0.5:0.5", // test node id is not an integer
                graph + "node:-1:coordinates:0.5:0.5", // test illegal node id
                graph + "node:10:coordinates:0.5:0.5", // test illegal node id
                graph + "NODE:1:COORDINATES:0.5:0.5", // test big letters
                graph + "n o d e : 1 : c o o r d i n a t e s : 1 : 1", // test spaces


                // node color
                graph + "nod:1:color:#ff00ff", // test misspelled command
                graph + "node:1:colore:#ff00ff", // test misspelled command
                graph + "node:10:color:#ff00ff", // test illegal node id
                graph + "node:-1:color:#ff00ff", // test illegal node id
                graph + "node:1:color.000:#ff00ff", // test node id is not an integer
                graph + "node:1:color:#ff00f", // test illegal color
                graph + "node:1:color:#ff00ff0", // test illegal color
                graph + "node:1:color:#fg00f0", // test illegal color
                graph + "node:1:color:#-1f0f00", // test illegal color
                graph + "node:1:color:f0ff00", // test illegal color missing #
                graph + "node:1:color:##ff00ff", // test illegal color too much #
                graph + "node:1:color:#ff00ff;", // test illegal color too much ;
                graph + "node:1:color#ff00ff", // test illegal color missing :
                graph + "node1:color:#ff00ff", // test illegal color missing :
                graph + "node:1color:#ff00ff", // test illegal color missing :
                graph + "node:1:color::#ff00ff", // test illegal color too much :
                graph + "node::1:color:#ff00ff", // test illegal color too much :
                graph + "node:1::color:#ff00ff", // test illegal color too much :
                graph + "node:1:color:#ff00ff:", // test illegal color too much :
                graph + "NODE:1:COLOR:#ff00ff", // test big letters
                graph + "n o d e : 1 : c o l o r : # f f 0 0 f f", // test spaces

                // node label
                graph + "nod:1:label:Test", // test misspelled command
                graph + "node:1:labe:Test", // test misspelled command
                graph + "node:10:label:Test", // test illegal node id
                graph + "node:-1:label:Test", // test illegal node id
                graph + "node:a:label:Test", // test illegal node id
                graph + "node:1.000:labe:Test", // test node id is not an integer
                graph + "node1:label:Test", // test missing :
                graph + "node:1label:Test", // test missing :
                graph + "node:1:labelTest", // test missing :
                graph + "node::1:label:Test", // test too much :
                graph + "node:1::label:Test", // test too much :
                graph + "NODE:1:LABEL:Test", // test big letters
                graph + "n o d e : 1 : l a b e l : T e s t", // test spaces

                // edge color
                graph + "edge:10:colore#ff00ff", // test misspelled command
                graph + "edge:10:color:#ff00ff", // test illegal edge id
                graph + "edge:-1:color:#ff00ff", // test illegal edge id
                graph + "edge:1.000:color:#ff00ff", // test edge id is not an integer
                graph + "edge:1:color:ff00ff", // test illegal color missing #
                graph + "edge:1:color:##ff00ff", // test illegal color too much #
                graph + ":1:#ff00f", // test illegal color too short
                graph + "edge:1:color:#ff00fff", // test illegal color too long
                graph + "edge:1:color:#ff00fg", // test illegal color illegal character
                graph + "edge:1:color:#ff00fz", // test illegal color illegal character
                graph + "edge:1:color:#-ff00ff", // test illegal color illegal character
                graph + "edge::1:color:#ff00ff", // test too many :
                graph + "edge:1::color:#ff00ff", // test too many :
                graph + "edge:1:color::#ff00ff", // test too many :
                graph + "edge1:color:#ff00ff", // test missing :
                graph + "edge:1color:#ff00ff", // test missing :
                graph + "edge:1:color#ff00ff", // test too many :
                graph + "edge:1:color:#ff00ff;", // test illegal character ;
                graph + "EDGE:1:COLOR:#ff00ff", // test big letters
                graph + "e d g e : 1 : c o l o r : # f f 0 0 f f", // test spaces

                // add node
                graph + "addNod:10:0.5:0.5", // test misspelled command
                graph + "addNode:1:0.5:0.5", // try to add node which exists
                graph + "addNode:100:0.5:0.5\naddNode:100:0.5:0.5", // try to add node twice
                graph + "addNode:100:0.5:0.z5", // try to add node with illegal coordinates
                graph + "addNode:100:0.z5:0.5", // try to add node with illegal coordinates
                graph + "addNode:1z00:0.5:0.5", // try to add node with illegal id
                graph + "addNode:1.000:0.5:0.5", // test node id is not an integer
                graph + "addNode:-1:0.5:0.5", // test negative node id
                graph + "addNode:100:0.5:1.5", // try to add node with coordinates out of range
                graph + "addNode:100:-0.5:0.5", // try to add node with negative coordinates
                graph + "addNode:100:0.5:0.5:0.5", // try to add node with too much coordinates
                graph + "addNode:100:0.5", // try to add node with too less coordinates
                graph + "addNode:100:0.5:0;5", // try to add node illegal character ;
                graph + "addNode:100:0.5:0.5;", // try to add node illegal character ;
                graph + "addNode:100:0.5:0,5", // try to add node illegal character ,
                graph + "addNode100:0.5:0.5", // try to add node missing :
                graph + "addNode:1000.5:0.5", // try to add node missing :
                graph + "addNode:100:0.50.5", // try to add node missing :
                graph + "addNode::100:0.5:0.5", // try to add node too much :
                graph + "addNode:100::0.5:0.5", // try to add node too much :
                graph + "addNode:100:0.5::0.5", // try to add node too much :
                graph + "addNode:100:0.5:0.5:", // try to add node too much :
                graph + "ADDNODE:100:0.5:0.5", // test big letters
                graph + "a d d N o d e : 5 : 0 . 5 : 1", // test spaces

                // remove node
                graph + "removeNod:1", // test misspelled command
                graph + "removeNode:1z", // try to remove node without valid id
                graph + "removeNode:100", // try to remove node which does not exist
                graph + "removeNode:1.000", // try to remove node id not an integer
                graph + "removeNode:-1", // test illegal negative id
                graph + "removeNode1", // test missing :
                graph + "removeNode::1", // test too much :
                graph + "removeNode:1:", // test too much :
                graph + "removeNode:1;", // test illegal character ;
                graph + "REMOVENODE:1", // test big letters
                graph + "r e m o v e N o d e : 1", // test spaces

                // add edge
                graph + "addEdg:1:1:2", // test misspelled command
                graph + "addEdge:1:1:2", // try to add edge which exists
                graph + "addEdge:100:1:2\naddEdge:100:1:2", // try to add edge twice
                graph + "addEdge:100:1:2\naddEdge:100:1:3", // try to add edge with too much parameters
                graph + "addEdge:100:1:z", // try to add edge with illegal node id
                graph + "addEdge:100:1:-1", // try to add edge with illegal node id
                graph + "addEdge:100:1:1.000", // try to add edge with illegal node id
                graph + "addEdge:100:1:1", // try to add edge with same nodes
                graph + "addEdge:100:1:100", // try to add edge with node which does not exist
                graph + "addEdge:100:1:2:3", // try to add edge with too much parameters
                graph + "addEdge:100:1", // try to add edge with too less parameters
                graph + "addEdge:100:1:2;", // try to add edge illegal character ;
                graph + ":addEdge:100:1:2", // try to add edge too much :
                graph + "addEdge::100:1:2", // try to add edge too much :
                graph + "addEdge:100::1:2", // try to add edge too much :
                graph + "addEdge:100:1::2", // try to add edge too much :
                graph + "addEdge100:1:2", // try to add edge missing :
                graph + "addEdge:1001:2", // try to add edge missing :
                graph + "addEdge:100:12", // try to add edge missing :
                graph + "ADDEDGE:5:1:3", // test big letters
                graph + "a d d E d g e : 5 : 1 : 3", // test spaces

                // remove edge
                graph + "removeEdg:1", // test misspelled command
                graph + "removeEdge:1z", // try to remove edge without valid id
                graph + "removeEdge:1.000", // try to remove edge id not an integer
                graph + "removeEdge:-1", // test illegal negative id
                graph + "removeEdge:100", // try to remove edge which does not exist
                graph + "removeEdge1", // test missing :
                graph + "removeEdge::1", // test too much :
                graph + "removeEdge:1:", // test too much :
                graph + "removeEdge:1;", // test illegal character ;
                graph + "REMOVEEDGE:5", // test big letters
                graph + "r e m o v e E d g e : 1" // test spaces

        ).map(Arguments::arguments);
    }

    private static Stream<Arguments> generateLegalFiles() {
        String graph = GRAPH + "\n###\n";
        return Stream.of(
                "graph G{\n}###", // empty graph and algorithm
                graph, // empty algorithm
                graph + "###", // empty step

                // node color
                graph + "node:1:color:#ff00ff", // test node color
                graph + "node:0001:color:#ff00ff", // leading zeros
                graph + "node:1:color:#ff00ff\nnode:1:color:#000000", // test color node twice
                graph + "node:1:color:#012345", // test random color
                graph + "node:1:color:#abcdef", // test random color
                graph + "node:1:color:#ABCDEF", // test big letters
                graph + "node:1:color:#FF00FF", // test big letters
                graph + "node:1:color:#abcdef\nnode:2:color:#000000", // test color two nodes

                // node label
                graph + "node:1:label:Test", // test node l:abel
                graph + "node:0001:label:Test", // leading zeros
                graph + "node:1:label:Test\nnode:1:label:newTest", // test label node twice
                graph + "node:1:label:123", // test integer label
                graph + "node:1:label:3,1415", // test double label
                graph + "node:1:label:Test\nnode:2:label:123", // test label two nodes
                graph + "node:1:label::Test", // : in label
                graph + "node:1:label:Test;", // ; in label
                graph + "node:1:label:spa ce in l a b e l ", // test spaces in label
                graph + "node:1:label:", // test empty label

                // node coordinates
                graph + "node:1:coordinates:1:1", // test node coordinates
                graph + "node:0001:coordinates:1:1", // leading zeros
                graph + "node:1:coordinates:1:1\nnode:1:coordinates:0:0", // test coordinates node twice
                graph + "node:1:coordinates:0.123:0.657", // test random coordinates
                graph + "node:1:coordinates:0.123:0.657\nnode:2:coordinates:0.000:0.000", // test coordinates two nodes

                // edge color
                graph + "edge:1:color:#ff00ff", // test edge color
                graph + "edge:0001:color:#ff00ff", // leading zeros
                graph + "edge:1:color:#ff00ff\nedge:1:color:#000000", // test color edge twice
                graph + "edge:1:color:#012345", // test random color
                graph + "edge:1:color:#ABCEDF", // test big letters
                graph + "edge:1:color:#FF00FF", // test big letters
                graph + "edge:1:color:#abcdef\nedge:2:color:#000000", // test color two edges

                // add node
                graph + "addNode:5:0.5:1", // test add node
                graph + "addNode:0005:0.5:1", // leading zeros
                graph + "addNode:5:0.5:1\naddNode:6:0.5:1", // test add two nodes
                graph + "addNode:5:0.123:0.987", // test random coordinates

                // remove node
                graph + "removeNode:1", // test remove node
                graph + "removeNode:0001", // leading zeros
                graph + "removeNode:1\nremoveNode:2", // test remove two nodes

                // add edge
                graph + "addEdge:5:1:3", // test add edge
                graph + "addEdge:0005:1:3", // leading zeros
                graph + "addEdge:5:0001:0003", // leading zeros
                graph + "addEdge:5:1:3\naddEdge:6:1:4", // test add two edges

                // remove edge
                graph + "removeEdge:1", // test remove edge
                graph + "removeEdge:0001", // leading zeros
                graph + "removeEdge:1\nremoveEdge:2", // test remove two edges

                // different steps
                graph + "node:1:color:#ff00ff\n###\nnode:1:color:#000000", // test two steps
                graph + "node:1:color:#ff00ff###node:1:color:#000000", // test two steps without line break
                graph + "node:1:color:#ffffff\n\n", // test empty line
                graph + "node:1:color:#ffffff\n###\n###\nnode:1:color:#000000" // test empty step

        ).map(Arguments::arguments);
    }

    @ParameterizedTest
    @MethodSource("generateIllegalFiles")
    void testInvalidFiles(String logFile) {
        assertThrows(ParseException.class, () -> new LogFileParser().parse(logFile));
    }

    @ParameterizedTest
    @MethodSource("generateLegalFiles")
    void testValidFiles(String logFile) {
        assertDoesNotThrow(() -> new LogFileParser().parse(logFile));
    }

    @ParameterizedTest
    @MethodSource("generateFiles")
    void testResultParser(String file, List<Graph> graphs) throws ParseException {

        Graph graph = graphs.get(0);

        LogFile logFile = new LogFileParser().parse(file);

        assertEquals(graph, logFile.graph());

        UserSession session = new UserSession();
        session.setLogFile(logFile);
        session.getAlgorithmManager().startAlgorithm(logFile.graph());

        // wait till executed
        for (int i = 0; !session.getAlgorithmManager().algorithmHasBeenExecuted(); i++) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // algorithm does not terminate
            assertTrue(i < 100);
        }
        for (int i = 1; i < graphs.size(); i++) {
            assertTrue(session.getAlgorithmManager().algorithmHasNext());
            graph.applyChanges(session.getAlgorithmManager().getNextStepOfAlgorithm());
            assertEquals(graphs.get(i), graph);
        }
        assertFalse(session.getAlgorithmManager().algorithmHasNext());
    }

    private static Stream<Arguments> generateFiles() throws ParseException {

        String file = """
                graph G{
                1[pos="0,0!"]
                2[pos="0.2,0.3!"]
                3[pos="1,0!"]
                4[pos="0,0.3!"]
                5[pos="0,1!"]
                1--2
                2--3
                3--4
                4--5
                1--3
                3--5
                }
                ###
                node:1:color:#ff00ff
                node:1:label:0
                ###
                node:2:coordinates:0.5:0.5
                edge:1:color:#ff8000
                ###
                addNode:6:0.5:1
                removeNode:2
                addEdge:7:1:6
                ###
                removeEdge:4
                ###""";

        List<Graph> graphs = new ArrayList<>();
        List<Node> nodes = List.of(
                new Node(1, new Coordinates(0, 0)),
                new Node(2, new Coordinates(0.2, 0.3)),
                new Node(3, new Coordinates(1, 0)),
                new Node(4, new Coordinates(0, 0.3)),
                new Node(5, new Coordinates(0, 1))
        );
        List<Edge> edges = List.of(
                new Edge(1, nodes.get(0), nodes.get(1)),
                new Edge(2, nodes.get(1), nodes.get(2)),
                new Edge(3, nodes.get(2), nodes.get(3)),
                new Edge(4, nodes.get(3), nodes.get(4)),
                new Edge(5, nodes.get(0), nodes.get(2)),
                new Edge(6, nodes.get(2), nodes.get(4))
        );
        graphs.add(new Graph(nodes, edges, false, false));
        Graph graph = graphs.get(0).clone();
        graph.getNodesMap().get(1).getNodeProperties().setColor(ColorConverter.getColor("#ff00ff"));
        graph.getNodesMap().get(1).getNodeProperties().setLabel("0");
        graphs.add(graph);
        graph = graph.clone();
        graph.getNodesMap().get(2).getNodeProperties().setCoordinates(new Coordinates(0.5, 0.5));
        graph.getEdgesMap().get(1).getEdgeProperties().setColor(ColorConverter.getColor("#ff8000"));
        graphs.add(graph);
        graph = graph.clone();
        graph.getNodesMap().put(6, new Node(6, new Coordinates(0.5, 1)));
        graph.removeNode(graph.getNodesMap().get(2));
        graph.getEdgesMap().put(7, new Edge(7, graph.getNodesMap().get(1), graph.getNodesMap().get(6)));
        graphs.add(graph);
        graph = graph.clone();
        graph.removeEdge(graph.getEdgesMap().get(4));
        graphs.add(graph);

        return Stream.of(arguments(file, graphs));
    }
}
