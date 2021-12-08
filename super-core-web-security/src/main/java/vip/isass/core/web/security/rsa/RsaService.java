package vip.isass.core.web.security.rsa;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RsaService {

    // todo 现在只有一个 id 为 PUBLIC 的秘钥对，优化成可按时间过期的的方式来动态变更秘钥对，方式长期使用同样的秘钥对
    // todo 秘钥对可放到 redis, 实现分布式加解密。需注意 redis 本身的安全访问，不能对外暴露，避免秘钥泄露
    // 通过 id 来分组，实现不同业务可用不同的秘钥对
    private static final Map<String, RsaKey> KEY_MAP = new ConcurrentHashMap<>();

    // 共用的 id，使用共用的密码对
    private static final String DEFAULT_ID = "PUBLIC";

    /**
     * 根据 id 加载秘钥对象，不存在则创建一个并缓存
     */
    private RsaKey loadKey(String id) {
        if (StrUtil.isBlank(id)) {
            id = DEFAULT_ID;
        }
        return KEY_MAP.computeIfAbsent(id, i -> {
            RSA rsa = SecureUtil.rsa();
            return RsaKey.builder()
                .rsa(rsa)
                .publicKeyStr(rsa.getPublicKeyBase64())
                .privateKeyStr(rsa.getPrivateKeyBase64())
                .build();
        });
    }

    public String getBase64PublicKey() {
        return getBase64PublicKey(null);
    }

    public String getBase64PublicKey(String id) {
        return loadKey(id).getPublicKeyStr();
    }

    public String encrypt(String plainText) {
        return encrypt(null, plainText);
    }

    public String encrypt(String id, String plainText) {
        Assert.notBlank(plainText, "plainText 必填");
        return loadKey(id).getRsa().encryptBase64(plainText, KeyType.PublicKey);
    }

    public String decrypt(String cipherText) {
        return decrypt(null, cipherText);
    }

    public String decrypt(String id, String cipherText) {
        Assert.notBlank(cipherText, "cipherText 必填");
        try {
            return SecureUtil
                .rsa(
                    loadKey(id).getRsa().getPrivateKey().getEncoded(),
                    null)
                .decryptStr(cipherText, KeyType.PrivateKey);
        } catch (Exception e) {
            throw new RuntimeException("无法解密密文，密文格式错误或秘钥不匹配");
        }
    }

}
