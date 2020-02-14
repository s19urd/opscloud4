package com.baiyi.opscloud.ldap.handler;


import com.baiyi.opscloud.ldap.config.LdapConfig;
import com.baiyi.opscloud.ldap.entry.Person;
import com.baiyi.opscloud.ldap.mapper.PersonAttributesMapper;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.naming.directory.*;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

/**
 * Ldap 通用处理
 */
@Slf4j
@Component
public class LdapHandler {

//    @Value("${spring.ldap.base}")
//    private String base;

//    @Value("${ldap.user.id}")
//    private String userId;
//
//    @Value("${ldap.user.baseDN}")
//    private String userBaseDN;
//
//    @Value("${ldap.user.objectClass}")
//    private String userObjectClass;

//    @Value("${ldap.group.id}")
//    private String groupId;
//
//    @Value("${ldap.group.baseDN}")
//    private String groupBaseDN;
//
//    @Value("${ldap.group.objectClass}")
//    private String groupObjectClass;

    @Resource
    private LdapTemplate ldapTemplate;

    @Resource
    private LdapConfig ldapConfig;

    /**
     * 查询所有Person
     *
     * @return
     */
    public List<Person> queryPersonList() {
        return ldapTemplate.search(query().where("objectclass").is( ldapConfig.getCustomByKey(LdapConfig.USER_OBJECT_CLASS)), new PersonAttributesMapper());
    }

    /**
     * 查询所有Person username
     *
     * @return
     */
    public List<String> queryPersonNameList() {

        return ldapTemplate.search(
                query().where("objectclass").is(ldapConfig.getCustomByKey(LdapConfig.USER_OBJECT_CLASS)), (AttributesMapper<String>) attrs -> (String) attrs.get(ldapConfig.getCustomByKey(LdapConfig.USER_ID)).get());
    }

    /**
     * 通过dn查询Person
     *
     * @param dn
     * @return
     */
    public Person getPersonWithDn(String dn) {
        return ldapTemplate.lookup(dn, new PersonAttributesMapper());
    }

    /**
     * 校验账户
     *
     * @param credential
     * @return
     */
    public boolean loginCheck(com.baiyi.opscloud.ldap.credential.PersonCredential credential) {
        if (credential.isEmpty()) return false;
        String username = credential.getUsername();
        String password = credential.getPassword();
        log.info("login check content username {}", username);
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(new EqualsFilter(ldapConfig.getCustomByKey(LdapConfig.USER_ID), username));
        try {
            boolean authResult = ldapTemplate.authenticate(ldapConfig.getCustomByKey(LdapConfig.USER_BASE_DN), filter.toString(), password);
            return authResult;
        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    /**
     * 解除绑定
     *
     * @param dn
     */
    public void unbind(String dn) {
        ldapTemplate.unbind(dn);
    }

    private boolean bind(String dn, Object obj, Attributes attrs) {
        try {
            ldapTemplate.bind(dn, obj, attrs);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 绑定用户
     *
     * @param person
     * @return
     */
    public boolean bindPerson(Person person) {
        String userId = ldapConfig.getCustomByKey(LdapConfig.USER_ID);
        String userBaseDN = ldapConfig.getCustomByKey(LdapConfig.USER_BASE_DN);
        String userObjectClass =ldapConfig.getCustomByKey(LdapConfig.USER_OBJECT_CLASS);

        try {
            String rdn = Joiner.on("=").join(userId, person.getUsername());
            String dn = Joiner.on(",").skipNulls().join(rdn, userBaseDN);
            // 基类设置
            BasicAttribute ocattr = new BasicAttribute("objectClass");
            ocattr.add("top");
            ocattr.add("person");
            ocattr.add("organizationalPerson");
            if (!userObjectClass.equalsIgnoreCase("person") && !userObjectClass.equalsIgnoreCase("organizationalPerson"))
                ocattr.add(userObjectClass);
            // 用户属性
            Attributes attrs = new BasicAttributes();
            attrs.put(ocattr);
            attrs.put(userId, person.getUsername()); // cn={username}
            attrs.put("sn", person.getUsername());
            attrs.put("displayName", person.getDisplayName());
            attrs.put("mail", person.getEmail());
            attrs.put("userPassword", person.getUserPassword());
            attrs.put("mobile", (StringUtils.isEmpty(person.getMobile()) ? "0" : person.getMobile()));
            if (bind(dn, null, attrs))
                return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updatePerson(Person person) {
        String rdn = Joiner.on("=").join( ldapConfig.getCustomByKey(LdapConfig.USER_ID), person.getUsername());
        String dn = Joiner.on(",").skipNulls().join(rdn, ldapConfig.getCustomByKey(LdapConfig.USER_BASE_DN));
        Person checkPerson = getPersonWithDn(dn);
        if (checkPerson == null) return false;
        try {
            if (!checkPerson.getDisplayName().equals(person.getDisplayName()))
                modifyAttributes(dn, "displayName", person.getDisplayName());
            if (!checkPerson.getEmail().equals(person.getEmail()))
                modifyAttributes(dn, "mail", person.getEmail());
            if (!checkPerson.getMobile().equals(person.getMobile()))
                modifyAttributes(dn, "mobile", person.getMobile());
            if (!StringUtils.isEmpty(person.getUserPassword()))
                modifyAttributes(dn, "userpassword", person.getUserPassword());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void modifyAttributes(String dn, String attrId, String value) {
        ldapTemplate.modifyAttributes(dn, new ModificationItem[]{
                new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(attrId, value))
        });
    }

}