#version 330

in vec2 pass_TextureCoords;
in int vertexX;
in int vertexY;

out vec4 out_Color;

uniform sampler2D sampler;
uniform vec4 textureCoords;
uniform vec4 color[4];

void main() {
	vec4 color = color[vertexX + vertexY * 2];
	vec2 coords = vec2(pass_TextureCoords.x * textureCoords.z + textureCoords.x, pass_TextureCoords.y * textureCoords.w + textureCoords.y);
	out_Color = color * texture(sampler, pass_TextureCoords);
}