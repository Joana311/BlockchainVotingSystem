using CentralPeerCoordinator.Contracts.Dtos;
using CentralPeerCoordinator.Contracts.Services;
using Microsoft.AspNetCore.Mvc;

namespace CentralPeerCoordinator.API.Controllers;

[ApiController]
[Route("api/peer")]
public class PeerController : ControllerBase
{
    private readonly IPeerService _peerService;

    public PeerController(IPeerService peerService)
    {
        _peerService = peerService;
    }

    [HttpGet("all", Name = "GetAllPeers")]
    public async Task<IActionResult> GetAllAsync()
    {
        return Ok(await _peerService.GetAllAsync());
    }

    [HttpGet("{id}", Name = "GetPeer")]
    public async Task<IActionResult> GetAsync([FromRoute] Guid id)
    {
        return Ok(await _peerService.GetAsync(id));
    }

    [HttpPost(Name = "CreatePeer")]
    public async Task<IActionResult> CreateAsync([FromBody] PeerRequestDto request)
    {
        return Ok(await _peerService.CreateAsync(request));
    }

    [HttpPut("{id}", Name = "UpdatePeer")]
    public async Task<IActionResult> UpdateAsync([FromRoute] Guid id, [FromBody] PeerRequestDto request)
    {
        return Ok(await _peerService.UpdateAsync(id, request));
    }

    [HttpDelete("{id}", Name = "DeletePeer")]
    public async Task<IActionResult> DeleteAsync([FromRoute] Guid id)
    {
        await _peerService.DeleteAsync(id);
        return Ok();
    }
}
