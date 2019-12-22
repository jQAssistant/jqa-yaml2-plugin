package com.buschmais.jqassistant.plugin.yaml2.impl.scanner.spec12;

import org.junit.jupiter.api.Test;

class ExampleC2E23IT extends AbstractYAMLPluginIT {
    private static String YAML_FILE = "/probes/example-c2-e23-various-explicit-tags.yaml";

    @Override
    String getSourceYAMLFile() {
        return YAML_FILE;
    }

    @Test
    void scannerCanReadDocument() {
        readSourceDocument();
    }

    /* Todo Write more specific tests */

}