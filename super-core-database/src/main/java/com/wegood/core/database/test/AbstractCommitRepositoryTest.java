package com.wegood.core.database.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractCommitRepositoryTest extends AbstractJUnit4SpringContextTests implements RepositoryTest {

    @Test
    @Override
    public void testInsert() {
        RepositoryTest.super.testInsert();
    }

    @Test
    @Override
    public void testBatchInsert() {
        RepositoryTest.super.testBatchInsert();
    }

    @Test
    @Override
    public void testDeleteById() {
        RepositoryTest.super.testDeleteById();
    }

    @Test
    @Override
    public void testDeleteByIds() {
        RepositoryTest.super.testDeleteByIds();
    }

    @Test
    @Override
    public void testUpdateById() {
        RepositoryTest.super.testUpdateById();
    }

    @Test
    @Override
    public void testUpdateByCriteria() {
        RepositoryTest.super.testUpdateByCriteria();
    }

    @Test
    @Override
    public void testGetById() {
        RepositoryTest.super.testGetById();
    }

    @Test
    @Override
    public void testFindByCriteria() {
        RepositoryTest.super.testFindByCriteria();
    }

    @Test
    @Override
    public void testGetAnyOneByCriteria() {
        RepositoryTest.super.testGetAnyOneByCriteria();
    }

}
