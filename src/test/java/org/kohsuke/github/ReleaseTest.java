package org.kohsuke.github;

import org.junit.Test;
import static org.junit.Assert.assertThrows;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class ReleaseTest extends AbstractGitHubWireMockTest {

    @Test
    public void testCreateSimpleRelease() throws Exception {
        GHRepository repo = gitHub.getRepository("hub4j-test-org/testCreateRelease");

        String tagName = UUID.randomUUID().toString();
        String releaseName = "release-" + tagName;

        GHRelease release = repo.createRelease(tagName).name(releaseName).prerelease(false).create();

        GHRelease releaseCheck = repo.getRelease(release.getId());

        assertThat(releaseCheck, notNullValue());
        assertThat(releaseCheck.getTagName(), is(tagName));
        assertThat(releaseCheck.isPrerelease(), is(false));
    }

    @Test
    public void testCreateDoubleReleaseFails() throws Exception{
        GHRepository repo = gitHub.getRepository("hub4j-test-org/testCreateRelease");

        String tagName = UUID.randomUUID().toString();
        String releaseName = "release-" + tagName;

        GHRelease release = repo.createRelease(tagName).name(releaseName).create();
        GHRelease releaseCheck = repo.getRelease(release.getId());
        assertThat(releaseCheck, notNullValue());

        HttpException httpException = assertThrows(
                HttpException.class, () -> {repo.createRelease(tagName).name(releaseName).create();});

        assertThat(httpException.getResponseCode(), is(422));
    }

}
