## Clock-On Helper
The Clock-On Helper helps you clock on automatically.

## Getting Started
Configure the options in the *clock-on.properties*.
The options are:

|    Field   |  Default Value | Description |
|:----------:|:-------------:|:------------|
|     name   |       | Your name. |
|     password   |       | Your password. |
|     use\_holiday\_calendar   |   true    | Use the holiday calendar from the open data for checking. The job is dismissed if today is a holiday. |
|     use\_holiday\_calendar   |  FireFox  | Could be *FireFox* or *HtmlUnit* |

Execute the jar by double click or by the command as
`java -jar clockon-helper.jar`.

## Notice
This helper doesn't include the scheduler. You can use crontab on linux or launchctl on mac OS X.
