package cn.com.cmplatform.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.com.cmplatform.service.User;
import cn.com.cmplatform.service.UserService;
import cn.com.cmplatform.gameplatform.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class LoginDBActivity extends Activity {

	private ListView listView;
	
	@Override
	public void onCreate(Bundle savedIntanceState)
	{
		super.onCreate(savedIntanceState);
		setContentView(R.layout.list);
		listView=(ListView) findViewById(R.id.listview);
		LoginSysExitApplication.getInstance().addActivity(this);
		
		final UserService service= new UserService(this);
		List<User> users=service.getScrollData(0, 10);
		final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
		
		for(User u:users)
		{
			HashMap<String,Object>item=new HashMap<String,Object>();
			item.put("id", String.valueOf(u.getId()));
			item.put("username", u.getUsername());
			item.put("password", u.getPassword());
			data.add(item);
		}
		
		final SimpleAdapter simple=new SimpleAdapter(this, data, R.layout.listview, new String[]{"id","username"}, new int[]{R.id.id,R.id.username});
		listView.setAdapter(simple);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// ListView list=(ListView) parent;
				Map<String,String>map=(Map<String, String>) listView.getItemAtPosition(position);
				Log.i("TAG",""+map.get("id"));
				boolean r=service.delete(Integer.valueOf(map.get("id")));
				Log.i("TAG", ""+position);
				if(r)
				{
					data.remove(position);
					simple.notifyDataSetChanged();
					Toast.makeText(LoginDBActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
				}else
				{
					Toast.makeText(LoginDBActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
