package work.cavaleiro.java.util.object;

import com.avaje.ebean.validation.NotNull;
import lombok.Getter;
import lombok.val;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Getter
public class ObjectString<Object> {

    private String code;
    private java.lang.Object object;

    public ObjectString(String code) {
        try {
            val inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(code));
            val dataInput = new BukkitObjectInputStream(inputStream);

            this.code = code;
            this.object = dataInput.readObject();

            dataInput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ObjectString(Object object) {
        try {
            val outputStream = new ByteArrayOutputStream();
            val dataOutput = new BukkitObjectOutputStream(outputStream);

            this.object = object;
            this.code = Base64Coder.encodeLines(outputStream.toByteArray());

            dataOutput.writeObject(object);
            dataOutput.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}