import com.itransition.life.core.ArrayLifeField;
import org.junit.Assert;
import org.junit.Test;

public class ArrayLifeFieldTestDigest {

    @Test
    public void testNoModifications() throws Exception {
        ArrayLifeField field = new ArrayLifeField(10,10);
        byte[] digest1 = field.getDigest();
        field.nextGeneration();
        byte[] digest2 = field.getDigest();
        Assert.assertArrayEquals(digest1, digest2);
    }

    @Test
    public void testStillLife() throws Exception {
        ArrayLifeField field = new ArrayLifeField(10,10);
        field.setState(1, 1, true);
        field.setState(1, 2, true);
        field.setState(2, 1, true);
        field.setState(2, 2, true);
        byte[] digest1 = field.getDigest();
        for (int i = 0; i < 100; i++) {
            field.nextGeneration();
        }
        byte[] digest2 = field.getDigest();
        Assert.assertArrayEquals(digest1, digest2);
    }

    @Test
    public void testPeriodicLife() throws Exception {
        ArrayLifeField field = new ArrayLifeField(10,10);
        field.setState(1, 1, true);
        field.setState(1, 2, true);
        field.setState(1, 3, true);
        byte[] digest1 = field.getDigest();
        for (int i = 0; i < 100; i++) {
            field.nextGeneration();
        }
        byte[] digest2 = field.getDigest();
        Assert.assertArrayEquals(digest1, digest2);
    }

}
