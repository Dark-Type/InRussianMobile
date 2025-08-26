
# UserTaskProgressItem

## Properties
| Name | Type | Description | Notes |
| ------------ | ------------- | ------------- | ------------- |
| **userId** | **kotlin.String** |  |  |
| **taskId** | **kotlin.String** |  |  |
| **status** | [**inline**](#Status) |  |  |
| **attemptCount** | **kotlin.Int** |  |  |
| **isCorrect** | **kotlin.Boolean** |  |  [optional] |
| **lastAttemptAt** | **kotlin.String** |  |  [optional] |
| **completedAt** | **kotlin.String** |  |  [optional] |
| **shouldRetryAfterTasks** | **kotlin.Int** |  |  [optional] |


<a id="Status"></a>
## Enum: status
| Name | Value |
| ---- | ----- |
| status | NOT_STARTED, IN_PROGRESS, COMPLETED, PENDING_RETRY |



