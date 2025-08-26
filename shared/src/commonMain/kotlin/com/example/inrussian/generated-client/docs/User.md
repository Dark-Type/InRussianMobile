
# User

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.String** |  |  |
| **email** | **kotlin.String** |  |  |
| **passwordHash** | **kotlin.String** |  |  |
| **role** | [**inline**](#Role) |  |  |
| **systemLanguage** | [**inline**](#SystemLanguage) |  |  |
| **status** | [**inline**](#Status) |  |  |
| **createdAt** | **kotlin.String** |  |  |
| **updatedAt** | **kotlin.String** |  |  |
| **phone** | **kotlin.String** |  |  [optional] |
| **avatarId** | **kotlin.String** |  |  [optional] |
| **lastActivityAt** | **kotlin.String** |  |  [optional] |


<a id="Role"></a>
## Enum: role
| Name | Value |
| ---- | ----- |
| role | STUDENT, EXPERT, CONTENT_MODERATOR, ADMIN |


<a id="SystemLanguage"></a>
## Enum: systemLanguage
| Name | Value |
| ---- | ----- |
| systemLanguage | RUSSIAN, UZBEK, CHINESE, HINDI, TAJIK, ENGLISH |


<a id="Status"></a>
## Enum: status
| Name | Value |
| ---- | ----- |
| status | ACTIVE, SUSPENDED, DEACTIVATED, PENDING_VERIFICATION |



