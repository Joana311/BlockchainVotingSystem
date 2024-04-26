namespace VotingApp.Contracts.Services;

public interface IMessageQueueService
{
    Task SendMessage(String message);
}
