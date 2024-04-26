using Azure.Identity;
using Azure.Storage.Queues;
using Microsoft.Extensions.Options;
using VotingApp.Contracts.Services;
using VotingApp.Contracts.Settings;

namespace VotingApp.Services;

public class AzureMessageQueueService : IMessageQueueService
{
    private readonly QueueClient _queueClient;
    public AzureMessageQueueService(IOptions<AzureStorageSettings> settings)
    {
        _queueClient = new QueueClient(
            new Uri($"https://{settings.Value.StorageAccountName}.queue.core.windows.net/{settings.Value.QueueName}"),
            new DefaultAzureCredential());
    }

    public async Task SendMessage(string message)
    {
        await _queueClient.SendMessageAsync(message);
    }
}
