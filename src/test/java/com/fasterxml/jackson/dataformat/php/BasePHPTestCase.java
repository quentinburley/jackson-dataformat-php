package com.fasterxml.jackson.dataformat.php;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link junit.framework.TestCase} for PHP data format.
 */
public abstract class BaseTestForPHP extends junit.framework.TestCase {
  private PhpFactory phpFactory;
  private ObjectMapper mapper;

  public BaseTestForPHP() {
    super();
    loadMapper();
  }

  public BaseTestForPHP(final String name) {
    super(name);
    loadMapper();
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    super.tearDown();
  }

  private void loadMapper() {
    phpFactory = new PhpFactory();
    mapper = new ObjectMapper(phpFactory);
  }

  public PhpFactory getPhpFactory() {
    return phpFactory;
  }

  public ObjectMapper getMapper() {
    return mapper;
  }
}
