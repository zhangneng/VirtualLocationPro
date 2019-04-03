package cn.com.cmplatform.platform.model;

public class ItemPerMsgCenterModel {
	private Integer id;
	private String msg;

	public ItemPerMsgCenterModel() {
		super();
	}

	public ItemPerMsgCenterModel(Integer id, String msg) {
		super();
		this.id = id;
		this.msg = msg;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ItemPerMsgCenterModel [id=" + id + ", msg=" + msg + "]";
	}
}
