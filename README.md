This mod can be added to other mods in the build.gradle like this:

Add the following maven repository to the repositories section:
```groovy
repositories {
	maven {
		url 'https://maven.mrbreaknfix.com/'
	}
}
```
And add the following dependency to the dependencies section:
```groovy
dependencies {
    implementation 'com.mrbreaknfix:NoReposts:1.0.0'
}
```

In order to configure it, add the following configuration in your mods `fabric.mod.json` "custom" section:
If you do not want a specific feature, do not add it to the configuration.

Defaults for incorrect / blacklisted origin messages have defaults, but allow you to override them.
```json
  "custom": {
    "noreposts:officialOriginB64Reg": "Base 64 encoded string of website where your mod should be downloaded from",
    "noreposts:allowedOriginsB64Reg": [
        "Regex encoded in base 64, of to check the host and referer of the download's origin",
        "ex: .modrinth.* in base 64 is: Lm1vZHJpbnRoLio=, which would be put here"
    ],
    "noreposts:disallowedNamesB64Reg": [
        "Base 64 encoded regex of- disallowed file names, ex 9minecraft usally contains 'Mod' in the name, so we can block it"
    ],
    "noreposts:blacklistedOriginsB64Reg": [
        "Base 64 encoded regex of specific origins to block, ex: .9minecraft.* which cannot trigger by mistake"
    ],
    "noreposts:incorrectOriginMessage": "Message to display when the origin is incorrect",
    "noreposts:blacklistedOriginMessage": "Message to display when the origin is blacklisted",
    "noreposts:incorrectNameMessage": "Message to display when the file name is incorrect"
    }
```

Here is a full, working example as implemented in UI-Utils
```json
  "custom": {
    "modmenu:clientsideOnly": true,
    "modmenu:api": false,

    "noreposts:officialOriginB64Reg": "aHR0cHM6Ly91aS11dGlscy5jb20=",
    "noreposts:allowedOriginsB64Reg": [
      "Lm1vZHJpbnRoLio=",
      "Xmh0dHBzPzpcL1wvdWktdXRpbHNcLmNvbShcL3xcPy4qKT8k"
    ],
    "noreposts:disallowedNamesB64Reg": [
      "LipNb2QuKg=="
    ],
    "noreposts:blacklistedOriginsB64Reg": [
      "Lio5bWluZWNyYWZ0Lio="
    ],
    "noreposts:incorrectOriginMessage": "Uh oh! Please be careful when downloading mods from unofficial sources, many websites are known to repost mods without permission, put malware into their mods, steal your data, and violate licenses.",
    "noreposts:blacklistedOriginMessage": "Uh oh! You downloaded UI-Utils from a blacklisted source! Blacklisted sources are known to host malware, and violate licenses.",
    "noreposts:incorrectNameMessage": "Hey there! This mod looks like it could have been tampered with. Please download it from the official source to ensure your safety."
  }
```



