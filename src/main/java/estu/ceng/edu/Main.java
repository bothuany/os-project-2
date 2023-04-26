package estu.ceng.edu;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws CloneNotSupportedException {
        Options options = new Options();
        CmdLineParser parser = new CmdLineParser(options);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }

        String fileName = options.fileName;

        List<Node> nodes = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(fileName))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    continue;
                }
                String[] tokens = line.split("->");

                if(tokens.length==1){
                    Node node = new Node(tokens[0]);
                    nodes.add(node);
                }
                else{
                    String[] dependencies = tokens[0].split(",");
                    Node node = new Node(tokens[1]);

                    addToNodes(nodes, dependencies, node);
                }
            }

            for(Node node:nodes){
                node.start();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void addToNodes(List<Node> nodes, String[] dependencies, Node node) {
        if(!nodes.contains(node)){
            addDependenciesToNodes(nodes, dependencies, node);
            nodes.add(node);
        }
        else{
            node = nodes.get(nodes.indexOf(node));
            addDependenciesToNodes(nodes, dependencies, node);
        }
    }

    private static void addDependenciesToNodes(List<Node> nodes, String[] dependencies, Node node) {
        for(String dependency : dependencies){
            Node depNode = new Node(dependency);
            if(!nodes.contains(depNode)){
                nodes.add(depNode);
                node.addDependency(depNode);
            }
            else{
                node.addDependency(nodes.get(nodes.indexOf(depNode)));
            }
        }
    }

}