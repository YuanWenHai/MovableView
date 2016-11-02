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
 //if moved distance more than 30% width of the view,it will remove automatically.
  ((RemovableView) view).setAutoRemoveMultiplier(0.3f);
  ```
## Dependency
Add it in your root build.gradle at the end of repositories:
```
allprojects {
		repositories {
			...
			maven { url "https://jitpack.io" }
		}
	}
  ```
  Add the dependency:
  ```
  dependencies {
	        compile 'com.github.YuanWenHai:MovableView:1.5'
	}
  ```
