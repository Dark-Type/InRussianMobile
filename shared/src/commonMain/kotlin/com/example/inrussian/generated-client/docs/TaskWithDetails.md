
# TaskWithDetails

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **id** | **kotlin.String** |  |  |
| **themeId** | **kotlin.String** |  |  |
| **name** | **kotlin.String** |  |  |
| **taskType** | [**inline**](#TaskType) |  |  |
| **question** | **kotlin.String** |  |  |
| **isTraining** | **kotlin.Boolean** |  |  |
| **orderNum** | **kotlin.Int** |  |  |
| **createdAt** | **kotlin.String** |  |  |
| **content** | [**kotlin.collections.List&lt;TaskContentItem&gt;**](TaskContentItem.md) |  |  |
| **answerOptions** | [**kotlin.collections.List&lt;TaskAnswerOptionItem&gt;**](TaskAnswerOptionItem.md) |  |  |
| **instructions** | **kotlin.String** |  |  [optional] |
| **answer** | [**TaskAnswerItem**](TaskAnswerItem.md) |  |  [optional] |


<a id="TaskType"></a>
## Enum: taskType
| Name | Value |
| ---- | ----- |
| taskType | LISTEN_AND_CHOOSE, READ_AND_CHOOSE, LOOK_AND_CHOOSE, MATCH_AUDIO_TEXT, MATCH_TEXT_TEXT |



