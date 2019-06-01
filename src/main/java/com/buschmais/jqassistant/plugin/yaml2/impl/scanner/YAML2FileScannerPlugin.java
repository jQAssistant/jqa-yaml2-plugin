package com.buschmais.jqassistant.plugin.yaml2.impl.scanner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.buschmais.jqassistant.core.scanner.api.Scanner;
import com.buschmais.jqassistant.core.scanner.api.ScannerPlugin.Requires;
import com.buschmais.jqassistant.core.scanner.api.Scope;
import com.buschmais.jqassistant.plugin.common.api.model.FileDescriptor;
import com.buschmais.jqassistant.plugin.common.api.scanner.AbstractScannerPlugin;
import com.buschmais.jqassistant.plugin.common.api.scanner.filesystem.FileResource;
import com.buschmais.jqassistant.plugin.yaml2.api.model.YAML2FileDescriptor;

import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.resolver.Resolver;

@Requires(FileDescriptor.class)
public class YAML2FileScannerPlugin extends AbstractScannerPlugin<FileResource, YAML2FileDescriptor> {

    /**
     * Supported file extensions for YAML file resources.
     */
    public final static String YAML_FILE_EXTENSION = ".yaml";
    public final static String YML_FILE_EXTENSION = ".yml";

    @Override
    public boolean accepts(FileResource file, String path, Scope scope) {
        String lowercasePath = path.toLowerCase();
        return lowercasePath.endsWith(YAML_FILE_EXTENSION) || lowercasePath.endsWith(YML_FILE_EXTENSION);
    }

    @Override
    public YAML2FileDescriptor scan(FileResource item, String path, Scope scope, Scanner scanner) throws IOException {

        return null;
    }

    /**
     * Non-resolving resolver to avoid automatic type conversion provided by
     * the used SnakeYAML libary.
     *
     * One good example for this disabled automatic type coversion is the
     * conversion of the string `OFF` to the boolean value `false`
     */
    private static class NonResolvingResolver extends Resolver {
        @Override
        protected void addImplicitResolvers() {
        }
    }


    class TagOverridingConstructor extends Constructor {
        private List<Tag> SUPPORTED_TAGS =
             Arrays.asList(Tag.YAML, Tag.MERGE,
                           Tag.SET, Tag.PAIRS, Tag.OMAP,
                           Tag.BINARY, Tag.INT, Tag.FLOAT,
                           Tag.BOOL, Tag.NULL,
                           Tag.STR, Tag.SEQ, Tag.MAP);

        @Override
        protected Object constructObject(Node node) {
            Tag tag = node.getTag();

            if (!SUPPORTED_TAGS.contains(tag)) {
                node.setTag(Tag.STR);
            }

            return super.constructObject(node);
        }
    }
}