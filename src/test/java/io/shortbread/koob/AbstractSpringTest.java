package io.shortbread.koob;

import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("springtest")
//@ContextConfiguration(initializers = ConfigurationPropertiesApplicationContextInitializer.class, classes = { PropertySourcesPlaceHolderConfig.class })
public abstract class AbstractSpringTest {
}
