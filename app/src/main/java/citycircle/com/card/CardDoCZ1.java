package citycircle.com.card;

import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import citycircle.com.R;
import util.BaseActivity;

/**
 * Created by X on 2016/11/9.
 */

public class CardDoCZ1 extends BaseActivity implements CompoundButton.OnCheckedChangeListener {

    private RadioGroup radioGroup;
    private TextView name;
    private TextView num;
    private int selectRadioId;
    private RadioButton selectRadio;

    private String id = "";
    private String sname = "";

    public void setSelectRadioId(int selectRadioId) {

        this.selectRadioId = selectRadioId;
        RadioButton btn = (RadioButton)findViewById(selectRadioId);
        selectRadio.setChecked(false);
        selectRadio = btn;
        num.setText(btn.getText().toString());

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if(isChecked)
        {
            setSelectRadioId(buttonView.getId());
        }

    }

    private void setCheckedEven(ViewGroup v)
    {
        for(int i=0;i<v.getChildCount();i++)
        {
            View temp = v.getChildAt(i);

            if(temp instanceof  RadioButton)
            {
                ((RadioButton) temp).setOnCheckedChangeListener(this);
            }

            if (temp instanceof ViewGroup)
            {
                setCheckedEven((ViewGroup)temp);
            }


        }
    }

    @Override
    protected void setupUi() {
        setContentView(R.layout.chongzhi1);
        setPageTitle("充值");

        id = getIntent().getStringExtra("id");
        sname = getIntent().getStringExtra("sname");

        selectRadioId = R.id.radio0;
        selectRadio = (RadioButton)findViewById(selectRadioId);
        radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        name = (TextView)findViewById(R.id.name);
        num = (TextView)findViewById(R.id.num);

        setCheckedEven(radioGroup);

        name.setText(sname);

    }

    @Override
    protected void setupData() {

    }

    public void submit(View v)
    {

    }
}
