package org.example;

import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Unit test for simple App.
 */
public class JsonTest {

    @Test
    public void calculatePatch() throws URISyntaxException, IOException {
        diff_match_patch dmp = new diff_match_patch();

        String oldValue = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/oldValue.json").toURI())));
        String newValue = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/newValue.json").toURI())));

        LinkedList<diff_match_patch.Patch> patch = dmp.patch_make(oldValue, newValue);

        assertThat(newValue).isEqualTo(dmp.patch_apply(patch, oldValue)[0]);
    }

    @Test
    public void calculatePatchNested() throws URISyntaxException, IOException {
        diff_match_patch dmp = new diff_match_patch();

        String oldValue = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/oldValue.json").toURI())));
        String newValue = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/newValueNested.json").toURI())));

        LinkedList<diff_match_patch.Patch> patch = dmp.patch_make(oldValue, newValue);

        assertThat(newValue).isEqualTo(dmp.patch_apply(patch, oldValue)[0]);
    }

    @Test
    public void calculatePatchNestedIteration() throws URISyntaxException, IOException {
        diff_match_patch dmp = new diff_match_patch();

        String oldValue = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/oldValue.json").toURI())));
        String newValueV1 = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/newValue.json").toURI())));
        String newValueV2 = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/newValueNested.json").toURI())));
        String newValueV3 = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("testData/newValueNestedDelete.json").toURI())));

        LinkedList<diff_match_patch.Patch> patchV1 = dmp.patch_make(oldValue, newValueV1);
        LinkedList<diff_match_patch.Patch> patchV2 = dmp.patch_make(newValueV1, newValueV2);
        LinkedList<diff_match_patch.Patch> patchV3 = dmp.patch_make(newValueV2, newValueV3);

        String afterFirstPatch = dmp.patch_apply(patchV1, oldValue)[0].toString();
        String afterSecondPatch = dmp.patch_apply(patchV2, afterFirstPatch)[0].toString();
        String afterThirdPatch = dmp.patch_apply(patchV3, afterSecondPatch)[0].toString();

        assertThat(newValueV3).isEqualTo(afterThirdPatch);
    }
}
