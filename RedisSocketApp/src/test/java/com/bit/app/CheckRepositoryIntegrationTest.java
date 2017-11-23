package com.bit.app;

import lombok.extern.slf4j.Slf4j;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CheckRepositoryIntegrationTest {

    private static final String name = "Igor Tester";


//    @Autowired
//    private CheckedRepository checkedRepository;

//    @Test
//    public void whenSavingCheckThenAvailbleOnRetrieval() {
//        log.warn("Start test saving ...");
//        CheckEntity checkEntity =  new CheckEntity(name);
//        checkedRepository.saveCheck(checkEntity);
//        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getName());
//        MatcherAssert.assertThat("", checkEntity.getName().equals(retrievedCheckEntity.getName()));
//    }
//
//    @Test
//    public void whenUpdatingCheckThenAvailableOnRetrieval() throws Exception {
//        log.warn("Start test updating ...");
//        CheckEntity checkEntity = new CheckEntity(name);
//        checkedRepository.saveCheck(checkEntity);
//        checkEntity.setName("Yana Tester");
//        checkedRepository.updateCheck(checkEntity);
//        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getName());
//        MatcherAssert.assertThat("", checkEntity.getName().equals(retrievedCheckEntity.getName()));
//    }
//
//    @Test
//    public void whenSavingCheckThenAllShouldAvailableOnRetrieval() throws Exception {
//        log.warn("Start test available all saving ...");
//        CheckEntity engCheckEntity = new CheckEntity(name);
//        CheckEntity medCheckEntity = new CheckEntity("Andrew Tester");
//        checkedRepository.saveCheck(engCheckEntity);
//        checkedRepository.saveCheck(medCheckEntity);
//        Map<Object, Object> retrievedCheck = checkedRepository.findAllChecked();
//        MatcherAssert.assertThat("", retrievedCheck.size() == 2);
//    }
//
//    @Test
//    public void whenDeletingCheckThenNotAvailableOnRetrieval() throws Exception {
//        log.warn("Start test delete ...");
//        CheckEntity checkEntity = new CheckEntity(name);
//        checkedRepository.saveCheck(checkEntity);
//        checkedRepository.deleteCheck(checkEntity.getName());
//        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getName());
//        assertNull(retrievedCheckEntity);
//    }
}
