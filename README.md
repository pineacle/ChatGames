# ChatGames (W.I.P)
Have your players compete in chat games that will test their quickness.

### Commands
/chatgames - Shows available commands (chatgames.help)<br>
/chatgames help - Shows available commands (chatgames.help)<br>
/chatgames toggle - Toggles the active state of the game (chatgames.toggle)<br>

### Soft-Dependencies
- Vault<br>
- Economy plugin (Essentials)

---

### For Developers

#### Using Maven
```xml
<repository>
  <id>jitpack-repo</id>
  <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.pineacle.ChatGames</groupId>
    <artifactId>chatgames-api</artifactId>
    <version>master-SNAPSHOT</version>
</dependency>
``` 

### plugin.yml
Add ChatGames as a (soft)-depend in your .yml
```yaml
soft-depend: [ChatGames]
``` 

### Using the API

```java
public IChatGames api = null;

@Override
public void onEnable() {
    if (Bukkit.getServer().getPluginManager().getPlugin("ChatGames") != null)
        api = (IChatGames) Bukkit.getServer().getPluginManager().getPlugin("ChatGames");

    if (api != null) api.getGameManager().register(Class<? extends Game>);

}
```

```java
public class ExampleGame implements Game {}
```
