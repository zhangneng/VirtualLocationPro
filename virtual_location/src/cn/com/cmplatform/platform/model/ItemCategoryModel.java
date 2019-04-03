package cn.com.cmplatform.platform.model;

/**
 * 左边底部类别服务 item的信息实体类
 * 
 * @author jiangqingqing
 * 
 */
public class ItemCategoryModel {
	private Integer id; // 图标的资源id
	private String name;// 类别item的名称
	private String title; // 类别的item的英文名称

	public ItemCategoryModel() {
		super();
	}

	public ItemCategoryModel(Integer id, String name, String title) {
		super();
		this.id = id;
		this.name = name;
		this.title = title;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "ItemCategoryModel [id=" + id + ", name=" + name + ", title="
				+ title + "]";
	}

}
