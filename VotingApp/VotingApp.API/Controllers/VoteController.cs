using Microsoft.AspNetCore.Mvc;
using VotingApp.Contracts.Services;

namespace VotingApp.API.Controllers;

[ApiController]
[Route("api/votes")]
public class VoteController : ControllerBase
{
    private readonly IMessageQueueService _messageQueueService;

    public VoteController(IMessageQueueService messageQueueService)
    {
        _messageQueueService = messageQueueService;
    }

    [HttpPost(Name = "CreateVote")]
    public async Task<IActionResult> CreateAsync([FromBody] String vote)
    {
        await _messageQueueService.SendMessage(vote);
        return Ok();
    }
}
