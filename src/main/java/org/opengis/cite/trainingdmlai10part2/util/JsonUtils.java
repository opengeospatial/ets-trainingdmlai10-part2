package org.opengis.cite.trainingdmlai10part2.util;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonUtils {
    public static List<JsonNode> findNodesByNames(JsonNode rootNode, String[] nodeNames) {
        List<JsonNode> matchingNodes = new ArrayList<>();

        if (rootNode == null || nodeNames == null || nodeNames.length == 0) {
            return matchingNodes;
        }

        if (rootNode.isObject()) {
            Iterator<String> fieldNames = rootNode.fieldNames();
            while (fieldNames.hasNext()) {
                String currentFieldName = fieldNames.next();

                for (String nodeName : nodeNames) {
                    if (currentFieldName.equals(nodeName)) {
                        JsonNode matchedNode = rootNode.get(currentFieldName);
                        matchingNodes.add(matchedNode);
                    }
                }

                matchingNodes.addAll(findNodesByNames(rootNode.get(currentFieldName), nodeNames));
            }
        }

        if (rootNode.isArray()) {
            for (JsonNode arrayElement : rootNode) {
                matchingNodes.addAll(findNodesByNames(arrayElement, nodeNames));
            }
        }

        return matchingNodes;
    }
}
