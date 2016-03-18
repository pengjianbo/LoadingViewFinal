package cn.finalteam.loadingviewfinal.sample.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.finalteam.loadingviewfinal.sample.R;
import cn.finalteam.loadingviewfinal.sample.http.model.GameInfo;

/**
 * Desction:
 * Author:pengjianbo
 * Date:16/3/10 上午11:01
 */
public class NewGameRvAdapter extends RecyclerView.Adapter<NewGameRvAdapter.NewGameRvViewHolder> {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<GameInfo> mGameList;

    public NewGameRvAdapter(Context context, List<GameInfo> gameList) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mGameList = gameList;
    }

    @Override
    public NewGameRvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.adapter_list_item, parent, false);
        return new NewGameRvViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewGameRvViewHolder holder, int position) {
        GameInfo gameInfo = mGameList.get(position);
        Picasso.with(mContext)
                .load(gameInfo.getIconUrl())
                .centerCrop()
                .placeholder(R.mipmap.ic_gf_default_photo)
                .error(R.mipmap.ic_gf_default_photo)
                .resize(300, 220)
                .config(Bitmap.Config.ARGB_8888)
                .into(holder.mIcGameIcon);
        holder.mTvGameName.setText(gameInfo.getName());
        holder.mRbGameRank.setRating(gameInfo.getTaskScore()/2.0f);
        holder.mTvGameSocre.setText(new DecimalFormat("#0.0").format(gameInfo.getTaskScore()) + "分");
        holder.mTvGamePlayerNumber.setText("热度:" + String.valueOf(gameInfo.getPlayerCount()));
        holder.mTvGameCommentNumber.setText("评论数:" + String.valueOf(gameInfo.getCommentCount()));
    }

    @Override
    public int getItemCount() {
        return mGameList.size();
    }

    static class NewGameRvViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ic_game_icon)
        ImageView mIcGameIcon;
        @Bind(R.id.tv_game_name)
        TextView mTvGameName;
        @Bind(R.id.rb_game_rank)
        RatingBar mRbGameRank;
        @Bind(R.id.tv_game_socre)
        TextView mTvGameSocre;
        @Bind(R.id.tv_game_player_number)
        TextView mTvGamePlayerNumber;
        @Bind(R.id.tv_game_comment_number)
        TextView mTvGameCommentNumber;

        public NewGameRvViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
