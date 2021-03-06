package com.buschmais.jqassistant.plugin.yaml2.impl.scanner.graph;

import com.buschmais.jqassistant.core.store.api.Store;
import com.buschmais.jqassistant.plugin.yaml2.api.model.YMLDocumentDescriptor;
import com.buschmais.jqassistant.plugin.yaml2.api.model.YMLMapDescriptor;
import com.buschmais.jqassistant.plugin.yaml2.api.model.YMLScalarDescriptor;
import com.buschmais.jqassistant.plugin.yaml2.api.model.YMLSequenceDescriptor;
import com.buschmais.jqassistant.plugin.yaml2.impl.scanner.parsing.AbstractBaseNode;
import com.buschmais.jqassistant.plugin.yaml2.impl.scanner.parsing.DocumentNode;

public class DocumentNodeProcessor
    implements NodeProcessor<DocumentNode, YMLDocumentDescriptor> {

    private final Store store;
    private final GraphGenerator generator;

    public DocumentNodeProcessor(Store store, GraphGenerator generator) {
        this.store = store;
        this.generator = generator;
    }

    @Override
    public void process(DocumentNode node, Callback<YMLDocumentDescriptor> callback, GraphGenerator.Mode mode) {
        YMLDocumentDescriptor documentDescriptor = store.create(YMLDocumentDescriptor.class);

        Callback<YMLSequenceDescriptor> callbackForSeq = descriptor -> documentDescriptor.getSequences().add(descriptor);
        Callback<YMLMapDescriptor> callbackForMaps = descriptor -> documentDescriptor.getMaps().add(descriptor);
        Callback<YMLScalarDescriptor> callbackForScalars = descriptor -> documentDescriptor.getScalars().add(descriptor);

        node.getSequences().forEach(sequenceNode -> generator.traverse(sequenceNode, callbackForSeq, mode));
        node.getMaps().forEach(mapNode -> generator.traverse(mapNode, callbackForMaps, mode));
        node.getScalars().forEach(scalarNode -> generator.traverse(scalarNode, callbackForScalars, mode));

        callback.created(documentDescriptor);
    }

    @Override
    public boolean accepts(AbstractBaseNode node) {
        return node.getClass().isAssignableFrom(DocumentNode.class);
    }
}
