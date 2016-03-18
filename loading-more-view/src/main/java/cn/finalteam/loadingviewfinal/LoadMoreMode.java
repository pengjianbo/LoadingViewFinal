package cn.finalteam.loadingviewfinal;

/**
 * Desction:加载更多模式
 * Author:pengjianbo
 * Date:16/3/7 下午6:03
 */
public enum LoadMoreMode {
    /**
     * 点击加载更多
     */
    CLICK,
    /**
     * 滑动到底部加载跟多
     */
    SCROLL;

    static LoadMoreMode mapIntToValue(int modeInt) {
        switch (modeInt) {
            case 0x0:
            default:
                return CLICK;
            case 0x1:
                return SCROLL;
        }
    }
}
