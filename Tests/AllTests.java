

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import snaptask.logic.TaskManagerTest;
import snaptask.parser.ParserTest;
import snaptask.storage.StorageTest;

@RunWith(Suite.class)
@SuiteClasses({ ParserTest.class, StorageTest.class, TaskManagerTest.class, TaskWindowTests.class })
public class AllTests {

}
