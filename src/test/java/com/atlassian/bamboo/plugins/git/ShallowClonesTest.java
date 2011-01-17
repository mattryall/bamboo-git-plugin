package com.atlassian.bamboo.plugins.git;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.testng.Assert.assertEquals;

public class ShallowClonesTest extends GitAbstractTest
{
    @DataProvider(parallel = true)
    Object[][] testShallowCloneData() throws Exception
    {
        return new Object[][]{
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/1.git", "1455", "shallow-clones/1-contents.zip"},
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/1.git", "1455", "shallow-clones/1-contents.zip"},
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "1455", "shallow-clones/1-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/1.git", "1455", "shallow-clones/1-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/5.git", "4c9d", "shallow-clones/2-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/2.git", "1455", "shallow-clones/1-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/2.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/2.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
        };
    }

//    final static Map<String, String[]> testShallowCloneDataMappings = new HashMap<String, String[]>() {{
//        put("1", new String[]{"git://github.com/pstefaniak/1.git", "1455", "shallow-clones/1-contents.zip"});
//        put("2", new String[]{"git://github.com/pstefaniak/2.git", "4c9d", "shallow-clones/2-contents.zip"});
//        put("3", new String[]{"git://github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"});
//    }};

    static final String[] protocols = new String[]{"git://", "https://"};

    @Test(dataProvider = "testShallowCloneData")
    public void testShallowClone(final String[][] successiveFetches) throws Exception
    {
        for (String protocol : protocols)
        {
            File tmp = createTempDirectory();
            GitOperationHelper helper = createGitOperationHelper();

            String revision = null;
            for (String[] currentFetch : successiveFetches)
            {
                helper.fetch(tmp, createAccessData(protocol + currentFetch[0]), true);
                revision = helper.checkout(tmp, currentFetch[1], revision);
                verifyContents(tmp, currentFetch[2]);
            }
        }
    }

    @DataProvider(parallel = true)
    Object[][] testShallowCloneDataFailing() throws Exception
    {
        return new Object[][]{
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "0a77", "shallow-clones/3-contents.zip"},
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/3.git", "4c9d", "shallow-clones/2-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/5.git", "0a77", "shallow-clones/3-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/5.git", "4c9d", "shallow-clones/2-contents.zip"},
                        },
                },
                {
                        new String[][]{
                                new String[]{"github.com/pstefaniak/5.git", "1455", "shallow-clones/1-contents.zip"},
                        },
                },
        };
    }

    @Test(dataProvider = "testShallowCloneDataFailing")
    public void testRepositoryHandlesFailingShallowClone(final String[][] successiveFetches) throws Exception
    {
        for (String protocol : protocols)
        {
            GitRepository gitRepository = createGitRepository();

            Field useShallowClones = GitRepository.class.getDeclaredField("USE_SHALLOW_CLONES");
            useShallowClones.setAccessible(true);
            useShallowClones.setBoolean(null, true);

            for (String[] currentFetch : successiveFetches)
            {
                setRepositoryProperties(gitRepository, protocol + currentFetch[0]);
                gitRepository.retrieveSourceCode(mockBuildContext(), currentFetch[1]);
                verifyContents(gitRepository.getSourceCodeDirectory(PLAN_KEY), currentFetch[2]);
            }
        }
    }

    @Test
    public void testShallowClone72Parents() throws Exception
    {
        File tmp = createTempDirectory();
        GitOperationHelper helper = createGitOperationHelper();

        helper.fetch(tmp, createAccessData("git://github.com/pstefaniak/72parents.git"), true);
        assertEquals(FileUtils.readLines(new File(tmp, ".git/shallow")).size(), 72);
        helper.checkout(tmp, "f9a3b37fcbf5298c1bfa", null);
        verifyContents(tmp, "shallow-clones/72parents-contents.zip");
    }

    @Test
    public void testDefaultFetchingToShallowedCopy() throws Exception
    {
        File tmp = createTempDirectory();
        GitOperationHelper helper = createGitOperationHelper();

        helper.fetch(tmp, createAccessData("git://github.com/pstefaniak/3.git"), true);
        assertEquals(FileUtils.readFileToString(new File(tmp, ".git/shallow")), "4c9d0c7e6167407deff1d31af5884911202dd3db\n");
        helper.fetch(tmp, createAccessData("git://github.com/pstefaniak/7.git"), false);
        assertEquals(FileUtils.readFileToString(new File(tmp, ".git/shallow")), "4c9d0c7e6167407deff1d31af5884911202dd3db\n");
        helper.checkout(tmp, "1070f438270b8cf1ca36", null);
        verifyContents(tmp, "shallow-clones/5-contents.zip");
    }

}
