package entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 系统用户
 *
 * @author liutao
 * @email ltsznh@gmail.com
 * @date 2016年9月18日 上午9:28:55
 */
public class SysUserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	private Long userId;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 盐
	 */
	private String salt;

	/**
	 * 邮箱
	 */

	private String email;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 状态  0：禁用   1：正常
	 */
	private Integer isEnabled;

	/**
	 * 角色ID列表
	 */
	private List<Long> roleIdList;


	/**
	 * 部门ID
	 */
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;


	private Date createDate;
	private Long creatorId;
	private String creatorName;

	private Date modifyDate;
	private Long modifierId;
	private String modifierName;

	/**
	 * 设置：
	 *
	 * @param userId
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * 获取：
	 *
	 * @return Long
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * 设置：用户名
	 *
	 * @param username 用户名
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * 获取：用户名
	 *
	 * @return String
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * 设置：密码
	 *
	 * @param password 密码
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * 获取：密码
	 *
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * 设置：邮箱
	 *
	 * @param email 邮箱
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 获取：邮箱
	 *
	 * @return String
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * 设置：手机号
	 *
	 * @param phone 手机号
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 获取：手机号
	 *
	 * @return String
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * 设置：状态  0：禁用   1：正常
	 *
	 * @param isEnabled 状态  0：禁用   1：正常
	 */
	public void setIsEnabled(Integer isEnabled) {
		this.isEnabled = isEnabled;
	}

	/**
	 * 获取：状态  0：禁用   1：正常
	 *
	 * @return Integer
	 */
	public Integer getIsEnabled() {
		return isEnabled;
	}

	public List<Long> getRoleIdList() {
		return roleIdList;
	}

	public void setRoleIdList(List<Long> roleIdList) {
		this.roleIdList = roleIdList;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public Long getModifierId() {
		return modifierId;
	}

	public void setModifierId(Long modifierId) {
		this.modifierId = modifierId;
	}

	public String getModifierName() {
		return modifierName;
	}

	public void setModifierName(String modifierName) {
		this.modifierName = modifierName;
	}
}
