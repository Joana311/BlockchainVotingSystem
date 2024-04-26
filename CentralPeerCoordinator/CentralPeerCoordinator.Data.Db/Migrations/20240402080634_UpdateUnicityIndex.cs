using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace CentralPeerCoordinator.Data.Db.Migrations
{
    /// <inheritdoc />
    public partial class UpdateUnicityIndex : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Peers_IpAddress",
                table: "Peers");

            migrationBuilder.CreateIndex(
                name: "IX_Peers_IpAddress_Port",
                table: "Peers",
                columns: new[] { "IpAddress", "Port" },
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Peers_IpAddress_Port",
                table: "Peers");

            migrationBuilder.CreateIndex(
                name: "IX_Peers_IpAddress",
                table: "Peers",
                column: "IpAddress",
                unique: true);
        }
    }
}
