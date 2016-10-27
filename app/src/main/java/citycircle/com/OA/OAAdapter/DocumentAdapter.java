/*
 * @Title FileListAdapter.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description：
 * @author Yann
 * @date 2015-8-9 上午11:37:18
 * @version 1.0
 */
package citycircle.com.OA.OAAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import citycircle.com.OA.entities.FileInfo;
import citycircle.com.OA.service.DownloadService;
import citycircle.com.R;
import citycircle.com.Utils.GlobalVariables;


/**
 * 类注释
 *
 * @author Yann
 * @date 2015-8-9 上午11:37:18
 */
public class DocumentAdapter extends BaseAdapter {
    private Context mContext;
    private List<FileInfo> mList;
    int type;
    Handler handler = new Handler();

    public DocumentAdapter(Context context, List<FileInfo> fileInfos, int type, Handler handler) {
        this.mContext = context;
        this.mList = fileInfos;
        this.type = type;
        this.handler = handler;
    }

    /**
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mList.size();
    }

    /**
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        final FileInfo fileInfo = mList.get(position);

        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();

            if (!viewHolder.mFileName.getTag().equals(Integer.valueOf(fileInfo.getId()))) {
                convertView = null;
            }
        }

        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.document_item, null);

            viewHolder = new ViewHolder(
                    (TextView) convertView.findViewById(R.id.name),
                    (ProgressBar) convertView.findViewById(R.id.press),
                    (Button) convertView.findViewById(R.id.dowm),
                    (ImageView) convertView.findViewById(R.id.type),
                    (Button) convertView.findViewById(R.id.del)
            );
            convertView.setTag(viewHolder);
            String name = fileInfo.getFileName();
            if (name.trim().toLowerCase().endsWith(".doc")) {
                viewHolder.type.setImageResource(R.drawable.icon_list_doc);
            } else if (name.trim().toLowerCase().endsWith(".ppt")) {
                viewHolder.type.setImageResource(R.drawable.icon_list_ppt);
            } else if (name.trim().toLowerCase().endsWith(".xls")) {
                viewHolder.type.setImageResource(R.drawable.icon_list_excel);
            } else if (name.trim().toLowerCase().endsWith(".txt")) {
                viewHolder.type.setImageResource(R.drawable.icon_list_txtfile);
            } else if (name.trim().toLowerCase().endsWith(".pdf")) {
                viewHolder.type.setImageResource(R.drawable.icon_list_pdf);
            } else {
                viewHolder.type.setImageResource(R.drawable.icon_list_unknown);
            }
            viewHolder.mFileName.setText(fileInfo.getFileName());
            viewHolder.mProgressBar.setMax(100);
            final ViewHolder finalViewHolder = viewHolder;
            final ViewHolder finalViewHolder1 = viewHolder;
            if (type == 0) {
                viewHolder.del.setVisibility(View.GONE);
            }
            viewHolder.mStartBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 通知Service开始下载
//                    if (fileInfo.getFinished()==0){
//                        finalViewHolder1.mStartBtn.setText("暂停");
                    finalViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(mContext, DownloadService.class);
                    intent.setAction(DownloadService.ACTION_START);
                    intent.putExtra("fileInfo", fileInfo);
                    mContext.startService(intent);
//                    }else {
//                        finalViewHolder1.mStartBtn.setText("下载");
//                        Intent intent = new Intent(mContext, DownloadService.class);
//                        intent.setAction(DownloadService.ACTION_STOP);
//                        intent.putExtra("fileInfo", fileInfo);
//                        mContext.startService(intent);
//                    }

                }
            });
//            viewHolder.mStopBtn.setOnClickListener(new OnClickListener()
//            {
//                @Override
//                public void onClick(View v)
//                {

//                }
//            });
            viewHolder.del.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalVariables.positions = position;
                    handler.sendEmptyMessage(5);
                }
            });
            // 将viewHolder.mFileName的Tag设为fileInfo的ID，用于唯一标识viewHolder.mFileName
            viewHolder.mFileName.setTag(Integer.valueOf(fileInfo.getId()));
        }

        viewHolder.mProgressBar.setProgress(fileInfo.getFinished());
        if (viewHolder.mProgressBar.getProgress() == 100) {
            viewHolder.mProgressBar.setVisibility(View.GONE);
            updateProgress(position, 0);
        }

        return convertView;
    }

    /**
     * 更新列表项中的进度条
     *
     * @param id
     * @param progress
     * @return void
     * @author Yann
     * @date 2015-8-9 下午1:34:14
     */
    public void updateProgress(int id, int progress) {
        FileInfo fileInfo = mList.get(id);
        fileInfo.setFinished(progress);
        notifyDataSetChanged();
    }

    private static class ViewHolder {
        TextView mFileName;
        ProgressBar mProgressBar;
        Button mStartBtn, del;
        ImageView type;

        /**
         * @param mFileName
         * @param mProgressBar
         * @param mStartBtn
         */
        public ViewHolder(TextView mFileName, ProgressBar mProgressBar,
                          Button mStartBtn, ImageView type, Button del) {
            this.mFileName = mFileName;
            this.mProgressBar = mProgressBar;
            this.mStartBtn = mStartBtn;
            this.type = type;
            this.del = del;
        }
    }

    /**
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        return null;
    }

    /**
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        return 0;
    }
}
