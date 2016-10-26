# MovableView
A movable view with removed callback.

![image](https://github.com/YuanWenHai/MovableView/blob/master/example/screenshot/anim.gif)

在view移出屏幕后回调onRemove方法.


## 使用：
```java
((RemovableView) view).setOnRemoveCallback(new RemovableView.OnRemoveCallback() {
                @Override
                public void onRemove(View view) {
                    list.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
 ```
## 修改自动移除因子：
 ```java
  ((RemovableView) view).setAutoRemoveMultiplier(0.3f);//移出屏幕超过30%松开后自动移除
  ```
