# MovableView
A movable view with removed callback.

![image](https://github.com/YuanWenHai/MovableView/blob/master/example/screenshot/anim.gif)


## USAGE：
```java
((RemovableView) view).setOnRemoveCallback(new RemovableView.OnRemoveCallback() {
                @Override
                public void onRemove(View view) {
                    list.remove(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
 ```
 ```java
  ((RemovableView) view).setAutoRemoveMultiplier(0.3f);//移出屏幕超过30%松开后自动移除
  ```
