namespace VotingApp.Contracts.Settings;

public class AzureStorageSettings
{
    public const string Section = "AzureStorage";

    public string StorageAccountName { get; set; } = default!;
    public string QueueName { get; set; } = default!;
}

