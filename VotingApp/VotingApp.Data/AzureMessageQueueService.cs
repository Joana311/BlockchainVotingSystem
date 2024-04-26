using VotingApp.Contracts.Services;

namespace VotingApp.Services;

public class AzureMessageQueueService : IMessageQueueService
{
    public Task SendMessage(string message)
    {
        throw new NotImplementedException();
    }
}
