package com.bit.app;

import com.bit.app.entities.CheckEntity;
import com.bit.app.repo.CheckedRepository;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertNull;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class CheckRepositoryIntegrationTest {

    private static final String id = "Eng2015001";
    private static final String name = "Igor Tester";


    @Autowired
    private CheckedRepository checkedRepository;

    @Test
    public void whenSavingCheckThenAvailbleOnRetrieval() {
        log.warn("Start test saving ...");
        CheckEntity checkEntity =  new CheckEntity(id, name, 1);
        checkedRepository.saveCheck(checkEntity);
        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getId());
        MatcherAssert.assertThat("", checkEntity.getId().equals(retrievedCheckEntity.getId()));
    }

    @Test
    public void whenUpdatingCheckThenAvailableOnRetrieval() throws Exception {
        log.warn("Start test updating ...");
        CheckEntity checkEntity = new CheckEntity(id, name, 1);
        checkedRepository.saveCheck(checkEntity);
        checkEntity.setName("Yana Tester");
        checkedRepository.updateCheck(checkEntity);
        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getId());
        MatcherAssert.assertThat("", checkEntity.getName().equals(retrievedCheckEntity.getName()));
    }

    @Test
    public void whenSavingCheckThenAllShouldAvailableOnRetrieval() throws Exception {
        log.warn("Start test available all saving ...");
        CheckEntity engCheckEntity = new CheckEntity(id, name, 1);
        CheckEntity medCheckEntity = new CheckEntity("Med2015001", "Andrew Tester", 2);
        checkedRepository.saveCheck(engCheckEntity);
        checkedRepository.saveCheck(medCheckEntity);
        Map<Object, Object> retrievedCheck = checkedRepository.findAllChecked();
        MatcherAssert.assertThat("", retrievedCheck.size() == 2);
    }

    @Test
    public void whenDeletingCheckThenNotAvailableOnRetrieval() throws Exception {
        log.warn("Start test delete ...");
        CheckEntity checkEntity = new CheckEntity(id, name, 1);
        checkedRepository.saveCheck(checkEntity);
        checkedRepository.deleteCheck(checkEntity.getId());
        CheckEntity retrievedCheckEntity = checkedRepository.findCheck(checkEntity.getId());
        assertNull(retrievedCheckEntity);
    }
}
