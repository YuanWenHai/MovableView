# MovableView
A movable view with removed callback.

![image](https://github.com/YuanWenHai/MovableView/blob/master/example/screenshot/anim.gif)


## Usage：
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
  ((RemovableView) view).setAutoRemoveMultiplier(0.3f);//if moved distance more than 30% width of the view,it will remove automatically.
  ```
