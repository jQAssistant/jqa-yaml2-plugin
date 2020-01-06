package com.buschmais.jqassistant.plugin.yaml2.impl.scanner.documents;

import java.util.List;

import com.buschmais.jqassistant.plugin.yaml2.api.model.*;
import com.buschmais.jqassistant.plugin.yaml2.impl.scanner.spec12.AbstractYAMLPluginIT;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.buschmais.jqassistant.plugin.yaml2.helper.TestHelper.*;
import static com.buschmais.jqassistant.plugin.yaml2.helper.YMLPluginAssertions.assertThat;

// todo Add a test with an alias as key of a map entry
public class AliasIT extends AbstractYAMLPluginIT {

    @Nested
    class TopLevelMap {
        @Test
        void thePluginHandlesAnAliasForSequenceProperlyAsValueForKeyProperly() {
            YMLFileDescriptor fileDescriptor = readSourceDocument("/alias/alias-toplevel-map-anchor-on-sequence.yml");

            YMLDocumentDescriptor document = getDocuments(fileDescriptor).getDocument(0);
            YMLMapDescriptor map = getMaps(document).getMap(0);
            YMLKeyDescriptor key = getKeys(map).getKeyByName("k2");

            assertThat(key).isNotNull().hasSequenceAsValue();

            YMLSequenceDescriptor sequenceDescriptor = (YMLSequenceDescriptor) key.getValue();

            assertThat(sequenceDescriptor).hasItems(3);
            YMLScalarDescriptor item0 = getScalars(sequenceDescriptor).getScalar(0);
            YMLScalarDescriptor item1 = getScalars(sequenceDescriptor).getScalar(1);
            YMLScalarDescriptor item2 = getScalars(sequenceDescriptor).getScalar(2);

            assertThat(item0).hasValue("s1v1").hasIndex(0);
            assertThat(item1).hasValue("s1v2").hasIndex(1);
            assertThat(item2).hasValue("s1v3").hasIndex(2);
        }

        @Test
        void cypherAliasOnASequenceCanBeFoundViaCypher() {
            readSourceDocument("/alias/alias-toplevel-map-anchor-on-sequence.yml");

            String cypherQuery = "MATCH (a:Anchor:Yaml:Sequence {" +
                                 "anchorName: 'alias' }) " +
                                 "RETURN a";

            List<Object> result = query(cypherQuery).getColumn("a");

            assertThat(result).hasSize(1);
        }

        @Test
        void thePluginHandlesAnAliasForAMapProperlyAsValueForKeyProperly() {
            YMLFileDescriptor fileDescriptor = readSourceDocument("/alias/alias-toplevel-map-anchor-on-map.yml");

            YMLDocumentDescriptor document = getDocuments(fileDescriptor).getDocument(0);
            YMLMapDescriptor map = getMaps(document).getMap(0);
            YMLKeyDescriptor key = getKeys(map).getKeyByName("kl2");

            assertThat(key).isNotNull().hasMapAsValue();

            YMLMapDescriptor mapDescriptor = (YMLMapDescriptor) key.getValue();

            assertThat(mapDescriptor).containsSimpleKeyWithName("kl2a");
            assertThat(mapDescriptor).containsSimpleKeyWithName("kl2b");
        }

        @Test
        void cypherAnchorOnAMapCanBeFoundViaCypher() {
            readSourceDocument("/alias/alias-toplevel-map-anchor-on-map.yml");

            String cypherQuery = "MATCH (a:Anchor:Yaml:Map {" +
                                 "anchorName: 'alias' }) " +
                                 "RETURN a";

            List<Object> result = query(cypherQuery).getColumn("a");

            assertThat(result).hasSize(1);
        }

    }

    @Nested
    class TopLevelSequence {
        @Test
        void thePluginHandlesInSequenceAnAliasForScalarProperly() {
            YMLFileDescriptor fileDescriptor = readSourceDocument("/alias/alias-toplevel-sequence-anchor-on-scalar.yml");

            YMLDocumentDescriptor document = getDocuments(fileDescriptor).getDocument(0);
            YMLSequenceDescriptor sequence = getSequences(document).getSequence(0);

            assertThat(sequence).hasItems();
            assertThat(sequence.getScalars()).hasSize(4);

            YMLScalarDescriptor aliasedScalar = getScalars(sequence).getScalar(2);
            assertThat(aliasedScalar).hasValue("bbbbb").hasIndex(2);
        }

        @Test
        void cypherAliasOnSequenceCanBeFoundViaCypher() {
            readSourceDocument("/alias/alias-toplevel-sequence-anchor-on-scalar.yml");

            String cypherQuery = "MATCH (a:Anchor:Yaml:Scalar {" +
                                 "anchorName: 'alias' }) " +
                                 "RETURN a";

            List<Object> result = query(cypherQuery).getColumn("a");

            assertThat(result).hasSize(1);
        }

    }








}