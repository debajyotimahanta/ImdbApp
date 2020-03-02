package imdb;

import com.imdb.dao.ImdbEntityDAO;
import com.imdb.dao.TitleRepository;
import com.imdb.dao.impl.Title;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTest {

    @Autowired
    public ImdbEntityDAO imdbEntityDAO;
    @Autowired
    private TitleRepository titleRepository;
    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    public void testRepo() {
        titleRepository.save(Title.builder()
                .tconst("1")
                .originalTitle("test")
                .build());
        assertNotNull(titleRepository.findById("1"));
        assertTrue(titleRepository.findById("2").isEmpty());
    }

    @Test
    public void givenACorrectSetup_thendaoWillBeAvailable() {
        assertNotNull(imdbEntityDAO);
    }


    @Test
    public void givenACorrectSetup_thenAnEntityManagerWillBeAvailable() {
        assertNotNull(testEntityManager);
    }
}
