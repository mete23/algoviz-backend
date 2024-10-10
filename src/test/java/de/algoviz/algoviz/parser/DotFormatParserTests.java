package de.algoviz.algoviz.parser;

import de.algoviz.algoviz.model.graph_general.graph.Coordinates;
import de.algoviz.algoviz.model.graph_general.graph.Graph;
import de.algoviz.algoviz.model.graph_general.graph.edge.Edge;
import de.algoviz.algoviz.model.graph_general.graph.node.Node;
import de.algoviz.algoviz.parser.graph_parser.DotFormatParser;
import de.algoviz.algoviz.parser.graph_parser.IGraphParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class DotFormatParserTests {
    private static Stream<Arguments> generateLegalFiles() {
        return Stream.of(
                "graph G{\n}",
                "graph G{\n1[pos=\"1,1!\"]\n}",
                """
                        graph G{
                        0001[pos="1,1!"]
                        }""", // leading zeros
                """
                        graph G{
                        1[pos="-1,1!"]
                        }""", // negative coordinate
                """
                        graph G{
                        1[pos="3.1415,1!"]
                        }""", // float coordinate
                """
                        graph G{
                        1[pos="1,1,1!"]
                        }""", // three coordinates
                """
                        graph G{
                        1[pos="1,1!"]
                        2[pos="1,1!"]
                        1--2 [weight=1]
                        }""", // one edge with weight
                """
                        graph G{
                        1[pos="1,1!"]
                        2[pos="1,1!"]
                        1--2 [weight=-1]
                        }""", // negative weight
                """
                        graph G{
                        1[pos="1,1!"]
                        2[pos="1,1!"]
                        1--2 [weight=-3.1415]
                        }""", // negative double weight
                """
                        graph G{
                        1[pos="1,1!"]
                        2[pos="1,1!"]
                        000001--2 [weight=-3.1415]
                        }""",// leading zeros
                """
                        gra ph      G{
                        1  [  pos =  "1,1  !"  ]
                        2  [  p o s =  " 1 , 1 ! "  ]
                        1  -  -  2  [  weig h t  =  - 3 . 1 4 1 5   ]
                        }""" // leading and trailing whitespaces

        ).map(Arguments::arguments);
    }

    private static Stream<Arguments> generateIllegalFiles() {
        return Stream.of(
                "",
                "test",
                "graphG{}",// missing new line
                "graph {}", // syntax error in graph name missing G
                "graph G{", // syntax error in graph missing }
                "graph G{\n]", // syntax error in graph missing }
                "graph G{\n}}", // syntax error in graph to many }
                "graph G{{\n}}", // syntax error in graph to many { and }
                "graph G{\n[]\n}", // syntax error in node missing id
                """ 
                        graph G{
                        1[pos="0.00.0"]
                        }""", // syntax error in coordinates missing ,
                """ 
                        graph G{
                        1[pos="0.0,,0.0"]
                        }""", // syntax error in coordinates to many ,
                """ 
                        graph G{
                        1[pos="0.0"]
                        }""", // syntax error in coordinates missing second coordinate
                """ 
                        graph G{
                        1[pos="0,0,0,0"]
                        }""", // syntax error in coordinates too many coordinates
                """
                        graph G{
                        1[po="0.0,0.0"]
                        }""", // syntax error missing s in pos
                """
                        graph G{
                        [pos="0.0,0.0"]
                        }""", // syntax error in node missing id
                """
                        graph G{
                        -1[pos="1,1!"]
                        }""", // negative id
                """
                        graph G{
                        1.000[pos="1,1!"]
                        """, // float id
                """
                        graph G{
                        1[pos="0.0,0.0"]
                        1[pos="0.0,0.0"]
                        }""", // syntax error in node already exists
                """
                        graph G{
                        1[pos="0.0,0.0"]
                        1[pos="1.0,1.0"]
                        }""", // syntax error in node id already exists
                """
                        graph G{
                        3,1415[pos="0.0,0.0"]
                        }""", // syntax error in node id is not an integer
                """
                        graph G{
                        1pos="0.0,0.0!"]
                        }""", // syntax error in node missing [
                """
                        graph G{
                        1[[pos="0.0,0.0!"]
                        }""", // syntax error in node to many [
                """
                        graph G{
                        1pos=[["0.0,0.0!"]]
                        }""", // syntax error in node to many [ and ]
                """
                        graph G{
                        1[pos"0.0,0.0!"]
                        }""",// syntax error in coordinates missing =
                """
                        graph G{
                        1[pos=="0.0,0.0!"]
                        }""",// syntax error in coordinates to many =
                """
                        graph G{
                        1[pos="0.0,0.0"]
                        }""",// syntax error in coordinates missing !
                """
                        graph G{
                        1[pos="0.0,0.0!!"]
                        }""",// syntax error in coordinates to many !
                """
                        graph G{
                        1[pos=0.0,0.0!]
                        }""",// syntax error in coordinates missing "
                """
                        graph G{
                        1[pos=0.0,0.0!"]
                        }""",// syntax error in coordinates missing "
                """
                        graph G{
                        1[pos=""0.0,0.0!"]
                        }""",// syntax error in coordinates to many "
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        1--
                        }""", // syntax error in edge missing node
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        1<-2
                        }""", // syntax error in edge illegal character <
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        1-2
                        }""", // syntax error in edge missing -
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        1__2
                        }""", // syntax error in edge missing -- illegal character _
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        3[pos="0.0,1.0!"]
                        1--2
                        2--3 [weight=]
                        }""", // weight and unweighted edges
                """
                        graph G{
                        1[pos="0.0,0.0!"]
                        2[pos="1.0,0.0!"]
                        3[pos="0.0,1.0!"]
                        1--2
                        2--3 [weight=1.0]
                        }""", // syntax error in edge missing first weight
                """
                        graph G{
                        1[pos="0.00000,0.10000!"]
                        2->1
                        }""", // first node of edge does not exist
                """
                        graph G{
                        1[pos="0.00000,0.10000!"]
                        1->2
                        }""", // second node of edge does not exist
                """
                        graph G{
                        1[pos="0.00000, 0.00000 !"]
                        2[pos=" 1.00000, 0.00000, 0.000 !"]""", // 2d and 3d coordinates
                """
                        graph G{
                        1[pos="0.00000,0.00000!"]
                        2[pos="1.00000,0.00000!"]
                        3[pos=".00000,1.00000!"]
                        1--2
                        2->3
                        }""", //directed and undirected edges
                """
                        graph G{
                        1[pos="0.00000,0.00000!"]
                        2[pos="1.00000,0.00000!"]
                        1--1
                        }
                        """, // edge with the same source and target
                """
                        graph G{
                        1[pos="0.00000,0.00000!"]
                        2[pos="1.00000,0.00000!"]
                        1--2 [weight=abc]
                        }
                        """, // edge label is not a double
                """
                        graph G{
                        1[pos="1,1!"]
                        2[pos="1,1!"]
                        1.00000--2 [weight=-3.1415]
                        }""" // trailing zeros
        ).map(Arguments::arguments);
    }

    @ParameterizedTest
    @MethodSource("generateLegalFiles")
    void testValidFiles(String graphFile) {
        assertDoesNotThrow(() -> new DotFormatParser().parse(graphFile));
    }

    @ParameterizedTest
    @MethodSource("generateIllegalFiles")
    void testInvalidFiles(String graphFile) {
        assertThrows(ParseException.class, () -> new DotFormatParser().parse(graphFile));
    }

    @Test
    public void testUndirectedUnweightedGraph() throws ParseException {
        IGraphParser parser = new DotFormatParser();

        String graph = "graph G{\n1[pos=\"0.00000,0.00000!\"]\n 2[pos=\"1.00000,0.00000!\"]\n3[pos=\".00000,1.00000!\"]\n 1--2\n 2--3\n}";

        List<Node> nodes = List.of(
                new Node(1, new Coordinates(0, 0)),
                new Node(2, new Coordinates(1, 0)),
                new Node(3, new Coordinates(0, 1))
        );
        List<Edge> edges = List.of(
                new Edge(1, nodes.get(0), nodes.get(1)),
                new Edge(2, nodes.get(1), nodes.get(2))
        );
        Graph expectedGraph = new Graph(nodes, edges, false, false);
        Graph parsedGraph = parser.parse(graph);

        assertEquals(expectedGraph, parsedGraph);
    }

    @Test
    public void testDirectedUnweightedGraph() throws ParseException {
        IGraphParser parser = new DotFormatParser();

        String graph = "graph G{\n1[pos=\"0.00000,0.10000!\"]\n 2[pos=\"-1.10000,0.00000!\"]\n3[pos=\".00000,1.00000!\"]\n 1->2\n 2->3\n}";

        List<Node> nodes = List.of(
                new Node(1, new Coordinates(0, 0.1)),
                new Node(2, new Coordinates(-1.1, 0)),
                new Node(3, new Coordinates(0, 1))
        );
        List<Edge> edges = List.of(
                new Edge(1, nodes.get(0), nodes.get(1)),
                new Edge(2, nodes.get(1), nodes.get(2))
        );
        Graph expectedGraph = new Graph(nodes, edges, true, false);
        Graph parsedGraph = parser.parse(graph);

        assertEquals(expectedGraph, parsedGraph);
    }

    @Test
    public void testUndirectedWeightedGraph() throws ParseException {
        IGraphParser parser = new DotFormatParser();

        String graph = "graph G{\n1[pos=\"0.00000,0.00000!\"]\n 2[pos=\"1.00000,0.00000!\"]\n3[pos=\".00000,1.00000!\"]\n 1--2[weight=-1]\n 2--3[weight=0.12]\n}";

        List<Node> nodes = List.of(
                new Node(1, new Coordinates(0, 0)),
                new Node(2, new Coordinates(1, 0)),
                new Node(3, new Coordinates(0, 1))
        );
        List<Edge> edges = List.of(
                new Edge(1, nodes.get(0), nodes.get(1), -1),
                new Edge(2, nodes.get(1), nodes.get(2), 0.12)
        );
        Graph expectedGraph = new Graph(nodes, edges, false, true);
        Graph parsedGraph = parser.parse(graph);

        assertEquals(expectedGraph, parsedGraph);
    }

    @Test
    public void testDirectedWeightedGraph() throws ParseException {
        IGraphParser parser = new DotFormatParser();

        String graph = "graph G{\n1[pos=\"0.00000,0.00000!\"]\n 2[pos=\"1.00000,0.00000!\"]\n3[pos=\".00000,1.00000!\"]\n 1->2[weight=-1]\n 2->3[weight=0.12]\n}";

        List<Node> nodes = List.of(
                new Node(1, new Coordinates(0, 0)),
                new Node(2, new Coordinates(1, 0)),
                new Node(3, new Coordinates(0, 1))
        );
        List<Edge> edges = List.of(
                new Edge(1, nodes.get(0), nodes.get(1), -1),
                new Edge(2, nodes.get(1), nodes.get(2), 0.12)
        );
        Graph expectedGraph = new Graph(nodes, edges, true, true);
        Graph parsedGraph = parser.parse(graph);

        assertEquals(expectedGraph, parsedGraph);
    }


}
