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

    public static List<JsonNode> findNodesByType(JsonNode node, String targetType) {
        List<JsonNode> result = new ArrayList<>();

        if (node.isObject()) {
            if (node.has("type") && node.get("type").asText().equals(targetType)) {
                result.add(node);
            }

            Iterator<JsonNode> elements = node.elements();
            while (elements.hasNext()) {
                JsonNode childNode = elements.next();
                result.addAll(findNodesByType(childNode, targetType));
            }
        } else if (node.isArray()) {
            for (JsonNode arrayElement : node) {
                result.addAll(findNodesByType(arrayElement, targetType));
            }
        }

        return result;
    }
}
