using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace CentralPeerCoordinator.Data.Db.Migrations
{
    /// <inheritdoc />
    public partial class MakeIpAddressUnique : Migration
    {
        /// <inheritdoc />
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AlterColumn<string>(
                name: "IpAddress",
                table: "Peers",
                type: "nvarchar(450)",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "nvarchar(max)");

            migrationBuilder.CreateIndex(
                name: "IX_Peers_IpAddress",
                table: "Peers",
                column: "IpAddress",
                unique: true);
        }

        /// <inheritdoc />
        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropIndex(
                name: "IX_Peers_IpAddress",
                table: "Peers");

            migrationBuilder.AlterColumn<string>(
                name: "IpAddress",
                table: "Peers",
                type: "nvarchar(max)",
                nullable: false,
                oldClrType: typeof(string),
                oldType: "nvarchar(450)");
        }
    }
}
