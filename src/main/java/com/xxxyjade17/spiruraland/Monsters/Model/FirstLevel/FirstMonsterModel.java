package com.xxxyjade17.spiruraland.Monsters.Model.FirstLevel;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.xxxyjade17.spiruraland.Monsters.Monster.FirstLevel.FirstMonster;
import com.xxxyjade17.spiruraland.SpiruraLand;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class FirstMonsterModel extends EntityModel<FirstMonster> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SpiruraLand.MODID, "first_monster"), "main");
    private final ModelPart group2;
    private final ModelPart group;


    public FirstMonsterModel(ModelPart root) {
        this.group2 = root.getChild("group2");
        this.group = root.getChild("group");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition group2 = partdefinition.addOrReplaceChild("group2", CubeListBuilder.create().texOffs(35, 0).addBox(-1.6203F, 2.6387F, -1.6444F, 9.0F, 1.6172F, 8.8984F, new CubeDeformation(0.0F))
                .texOffs(44, 0).addBox(0.5463F, 2.6387F, -4.5819F, 4.6668F, 0.6797F, 2.9375F, new CubeDeformation(0.0F))
                .texOffs(62, 54).addBox(1.9721F, 6.0806F, -4.5819F, 1.8153F, 0.6797F, 2.9375F, new CubeDeformation(0.0F))
                .texOffs(47, 59).addBox(1.8177F, 4.6387F, -4.1054F, 2.124F, 1.4531F, 2.4531F, new CubeDeformation(0.0F))
                .texOffs(63, 47).addBox(1.2572F, 3.3067F, -4.1054F, 3.245F, 1.332F, 2.4531F, new CubeDeformation(0.0F))
                .texOffs(51, 29).addBox(0.1436F, 7.0623F, 0.509F, 5.4722F, 1.6484F, 4.6367F, new CubeDeformation(0.0F))
                .texOffs(54, 34).addBox(0.5463F, 1.0215F, -0.7929F, 4.6668F, 1.6172F, 6.6836F, new CubeDeformation(0.0F))
                .texOffs(54, 0).addBox(-2.1027F, 4.8028F, -1.6444F, 9.9648F, 0.3906F, 7.8672F, new CubeDeformation(0.0F))
                .texOffs(3, 61).addBox(1.9057F, 2.4356F, -3.1757F, 1.948F, 0.2031F, 0.3086F, new CubeDeformation(0.0F))
                .texOffs(18, 66).addBox(2.4917F, 2.4434F, -3.5741F, 0.7761F, 0.1953F, 0.3984F, new CubeDeformation(0.0F))
                .texOffs(0, 22).addBox(-0.5463F, 0.7598F, 1.2071F, 1.0926F, 1.8789F, 1.0926F, new CubeDeformation(0.0F))
                .texOffs(4, 25).addBox(5.2132F, 0.7598F, 1.2071F, 1.0926F, 1.8789F, 1.0926F, new CubeDeformation(0.0F))
                .texOffs(7, 48).addBox(1.2747F, 2.6387F, 7.254F, 3.21F, 1.6172F, 0.8945F, new CubeDeformation(0.0F))
                .texOffs(54, 0).addBox(-2.1027F, 5.7871F, -1.6444F, 9.9648F, 0.3906F, 7.8672F, new CubeDeformation(0.0F)), PartPose.offset(-2.8797F, 5.166F, -1.8595F));

        PartDefinition cube_r1 = group2.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(28, 51).addBox(-0.9955F, -0.9955F, -2.5156F, 1.9911F, 1.991F, 5.125F, new CubeDeformation(0.0F))
                .texOffs(10, 51).mirror().addBox(-6.755F, -0.9955F, -2.5156F, 1.9911F, 1.991F, 5.125F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(5.7595F, -2.6788F, 10.9633F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r2 = group2.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(14, 28).addBox(-0.4473F, -0.8047F, -1.3316F, 0.6523F, 1.6094F, 1.0108F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.8107F, 3.4473F, 8.2737F, 0.0F, 0.3927F, 0.0F));

        PartDefinition cube_r3 = group2.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(10, 27).addBox(-0.2051F, -0.8047F, 0.5942F, 0.6523F, 1.6094F, 1.0108F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6857F, 3.4473F, 6.4946F, 0.0F, -0.3927F, 0.0F));

        PartDefinition cube_r4 = group2.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(43, 43).addBox(-0.5502F, -0.5463F, -3.3359F, 1.1004F, 1.0926F, 13.8399F, new CubeDeformation(0.0F))
                .texOffs(41, 43).addBox(-6.3097F, -0.5463F, -3.3359F, 1.1004F, 1.0926F, 13.8399F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(5.7595F, 0.0F, 4.4961F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r5 = group2.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(66, 66).addBox(0.0182F, -0.0977F, -0.1543F, 0.9558F, 0.1992F, 0.418F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0559F, 2.5371F, -3.7553F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r6 = group2.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(49, 39).addBox(-0.1543F, -0.0977F, -0.4076F, 0.4258F, 0.1992F, 1.3816F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.7036F, 2.5371F, -3.7553F, 0.0F, -0.7854F, 0.0F));

        PartDefinition cube_r7 = group2.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(31, 34).addBox(-4.0171F, -0.7266F, -3.0625F, 7.1748F, 1.5352F, 2.5273F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3095F, -4.478F, 6.6042F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r8 = group2.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(59, 53).addBox(-4.0171F, -0.7266F, -1.957F, 7.1748F, 1.5352F, 1.4219F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3095F, -2.9861F, 3.0025F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r9 = group2.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(54, 38).addBox(-4.0171F, -0.7266F, -0.1367F, 0.8467F, 1.5352F, 1.3906F, new CubeDeformation(0.0F))
                .texOffs(50, 6).addBox(-10.3453F, -0.7266F, -0.1367F, 0.8467F, 1.5352F, 1.3906F, new CubeDeformation(0.0F))
                .texOffs(42, 46).mirror().addBox(-8.1379F, -0.7266F, -0.1367F, 2.76F, 1.5352F, 1.3906F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(9.6376F, -2.8337F, 2.6344F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r10 = group2.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(52, 24).addBox(-4.0171F, -0.9883F, -1.957F, 7.1748F, 1.7969F, 5.3203F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.3095F, -1.326F, 3.6901F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r11 = group2.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(7, 45).addBox(-3.5791F, -2.5234F, 2.7031F, 5.643F, 2.5078F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.6374F, 2.0769F, -2.3714F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r12 = group2.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(18, 63).addBox(-1.6338F, -2.5234F, 4.0352F, 2.1352F, 0.8242F, 0.9102F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.446F, 1.3154F, -2.6868F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r13 = group2.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(60, 60).addBox(0.7095F, -0.8086F, -0.3867F, 1.6239F, 1.6172F, 2.3438F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.3582F, 2.9771F, -1.6049F, 0.7854F, 0.0F, 0.0F));

        PartDefinition cube_r14 = group2.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(48, 14).mirror().addBox(-0.7339F, 2.5371F, 1.543F, 2.7886F, 3.6484F, 1.7852F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.2193F, 10.1152F, -3.1818F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r15 = group2.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(4, 40).mirror().addBox(-1.3589F, 2.5371F, 0.6953F, 3.8394F, 3.6914F, 4.0781F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.319F, 9.1077F, -0.7494F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r16 = group2.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(10, 7).mirror().addBox(-1.3589F, 6.1895F, -1.707F, 3.8394F, 3.8672F, 6.4805F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.319F, 9.133F, -0.7389F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r17 = group2.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(57, 18).addBox(-1.3589F, 2.0059F, -1.707F, 3.8394F, 0.5391F, 6.4805F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.319F, 9.1005F, -0.7523F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r18 = group2.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(5, 19).mirror().addBox(-2.8667F, 2.127F, 3.9336F, 5.9175F, 2.0F, 3.832F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.7877F, 11.2333F, -0.4359F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r19 = group2.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(35, 18).addBox(-2.3433F, -0.8086F, -8.6621F, 5.48F, 1.6172F, 8.5742F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.483F, 7.7757F, 5.5363F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r20 = group2.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(5, 34).addBox(-2.3433F, -0.8086F, -4.7246F, 5.48F, 1.793F, 4.2891F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(2.483F, 7.6426F, 1.2208F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r21 = group2.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(40, 53).addBox(-0.3398F, -2.3334F, -0.9551F, 0.6797F, 3.7254F, 2.9297F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0062F, 5.3441F, -3.6229F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r22 = group2.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(22, 14).addBox(-0.3398F, -2.3334F, -0.9551F, 0.6797F, 3.7254F, 2.9297F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.7532F, 5.3441F, -3.6229F, 0.0F, 0.0F, -0.3927F));

        PartDefinition group = partdefinition.addOrReplaceChild("group", CubeListBuilder.create().texOffs(37, 58).addBox(21.5137F, -14.3604F, 0.5625F, 1.0664F, 3.4219F, 5.9453F, new CubeDeformation(0.0F))
                .texOffs(12, 58).mirror().addBox(18.9004F, -13.4425F, 0.5625F, 1.0078F, 2.5039F, 5.9453F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(59, 65).addBox(16.8779F, -19.9078F, 2.3555F, 1.0742F, 3.1914F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(9, 47).addBox(17.9391F, -19.5072F, 2.3555F, 1.0391F, 1.5F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(49, 65).addBox(20.7372F, -18.3375F, 2.3555F, 1.0742F, 1.4922F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(15, 65).addBox(21.7985F, -17.9368F, 2.3555F, 1.0391F, 1.5F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(13, 60).addBox(22.5636F, -15.7192F, 2.3555F, 0.9297F, 6.2539F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(32, 65).addBox(23.4933F, -15.7192F, 2.3555F, 1.0938F, 6.625F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(32, 33).mirror().addBox(18.3731F, -10.1104F, 0.5586F, 3.7578F, 1.2656F, 6.4727F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(59, 50).addBox(19.8887F, -12.9815F, 0.5625F, 1.6289F, 1.0859F, 5.9453F, new CubeDeformation(0.0F))
                .texOffs(36, 10).addBox(18.3731F, -8.8448F, 4.6875F, 3.7578F, 7.293F, 2.3438F, new CubeDeformation(0.0F))
                .texOffs(43, 43).mirror().addBox(18.3731F, -8.8448F, 1.375F, 3.7578F, 1.9648F, 3.3203F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(5, 29).mirror().addBox(18.3731F, -5.0466F, 2.339F, 3.7578F, 0.5F, 2.3594F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(37, 27).addBox(18.3731F, -4.5518F, 3.5781F, 3.7578F, 3.0F, 1.1094F, new CubeDeformation(0.0F))
                .texOffs(32, 0).mirror().addBox(18.9512F, -6.9268F, 2.9063F, 2.6016F, 1.8984F, 1.7813F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(6, 40).addBox(19.6035F, -1.5518F, 5.5508F, 1.2656F, 1.5078F, 1.2031F, new CubeDeformation(0.0F))
                .texOffs(12, 42).mirror().addBox(19.6035F, -1.5518F, 3.9219F, 1.2656F, 1.5078F, 1.2031F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(56, 56).addBox(19.8887F, -11.8956F, 1.7539F, 1.6289F, 1.8086F, 3.5547F, new CubeDeformation(0.0F))
                .texOffs(66, 47).addBox(22.5801F, -13.0401F, 5.0586F, 0.2695F, 0.9414F, 1.1211F, new CubeDeformation(0.0F))
                .texOffs(50, 66).addBox(22.5801F, -13.0401F, 0.8906F, 0.2695F, 0.9414F, 1.1211F, new CubeDeformation(0.0F))
                .texOffs(50, 50).addBox(15.3414F, -16.7148F, 0.5625F, 1.5547F, 2.1367F, 5.9453F, new CubeDeformation(0.0F))
                .texOffs(0, 57).addBox(0.4332F, -14.3604F, 0.5625F, 1.0664F, 3.4219F, 5.9453F, new CubeDeformation(0.0F))
                .texOffs(35, 57).mirror().addBox(3.105F, -13.4425F, 0.5625F, 1.0078F, 2.5039F, 5.9453F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(49, 64).addBox(5.0612F, -19.9078F, 2.3555F, 1.0742F, 3.1914F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(38, 64).addBox(4.0351F, -19.5072F, 2.3555F, 1.0391F, 1.5F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(64, 37).addBox(1.2018F, -18.3375F, 2.3555F, 1.0742F, 1.4922F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(49, 54).addBox(0.1757F, -17.9368F, 2.3555F, 1.0391F, 1.5F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(21, 64).addBox(-0.48F, -15.7192F, 2.3555F, 0.9297F, 6.2539F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(1, 6).addBox(-1.5738F, -15.7192F, 2.3555F, 1.0938F, 6.625F, 2.2344F, new CubeDeformation(0.0F))
                .texOffs(57, 13).addBox(0.8824F, -10.1104F, 0.5586F, 3.7578F, 1.2656F, 6.4727F, new CubeDeformation(0.0F))
                .texOffs(55, 47).addBox(1.4957F, -12.9815F, 0.5625F, 1.6289F, 1.0859F, 5.9453F, new CubeDeformation(0.0F))
                .texOffs(28, 55).addBox(0.8824F, -8.8448F, 4.6875F, 3.7578F, 7.293F, 2.3438F, new CubeDeformation(0.0F))
                .texOffs(13, 47).mirror().addBox(0.8824F, -8.8448F, 1.375F, 3.7578F, 1.9648F, 3.3203F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(17, 55).addBox(0.8824F, -5.0466F, 2.339F, 3.7578F, 0.5F, 2.3594F, new CubeDeformation(0.0F))
                .texOffs(24, 23).addBox(0.8824F, -4.5518F, 3.5781F, 3.7578F, 3.0F, 1.1094F, new CubeDeformation(0.0F))
                .texOffs(36, 58).addBox(1.4605F, -6.9268F, 2.9063F, 2.6016F, 1.8984F, 1.7813F, new CubeDeformation(0.0F))
                .texOffs(22, 30).addBox(2.1441F, -1.5518F, 5.5508F, 1.2656F, 1.5078F, 1.2031F, new CubeDeformation(0.0F))
                .texOffs(65, 31).addBox(2.1441F, -1.5518F, 3.9219F, 1.2656F, 1.5078F, 1.2031F, new CubeDeformation(0.0F))
                .texOffs(55, 55).addBox(1.4957F, -11.8956F, 1.7539F, 1.6289F, 1.8086F, 3.5547F, new CubeDeformation(0.0F))
                .texOffs(33, 34).addBox(0.1636F, -13.0401F, 5.0586F, 0.2695F, 0.9414F, 1.1211F, new CubeDeformation(0.0F))
                .texOffs(36, 37).addBox(0.1636F, -13.0401F, 0.8906F, 0.2695F, 0.9414F, 1.1211F, new CubeDeformation(0.0F))
                .texOffs(46, 48).mirror().addBox(6.1172F, -16.7148F, 0.5625F, 1.5547F, 2.1367F, 5.9453F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-11.5066F, 22.3761F, -3.3477F));

        PartDefinition cube_r23 = group.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(65, 38).addBox(-0.666F, -1.7695F, -3.3848F, 1.4219F, 1.5352F, 1.2695F, new CubeDeformation(0.0F))
                .texOffs(46, 17).addBox(-3.0762F, -1.5156F, -3.166F, 6.1523F, 2.3242F, 5.9531F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.8552F, -14.1374F, 3.7246F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r24 = group.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(19, 52).addBox(-1.2969F, -1.0938F, -0.8906F, 2.5938F, 2.043F, 0.7227F, new CubeDeformation(0.0F))
                .texOffs(6, 0).mirror().addBox(16.1938F, -1.0938F, -0.8906F, 2.5938F, 2.043F, 0.7227F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.7613F, -6.2462F, 3.3658F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r25 = group.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(22, 7).mirror().addBox(-1.875F, 1.416F, -0.6328F, 3.75F, 3.2305F, 1.2656F, new CubeDeformation(0.0F)).mirror(false)
                .texOffs(65, 5).addBox(-1.875F, -3.2363F, -0.6328F, 3.75F, 2.1328F, 1.2656F, new CubeDeformation(0.0F))
                .texOffs(35, 20).addBox(15.6157F, 1.416F, -0.6328F, 3.75F, 3.2305F, 1.2656F, new CubeDeformation(0.0F))
                .texOffs(3, 9).mirror().addBox(15.6157F, -3.2363F, -0.6328F, 3.75F, 2.1328F, 1.2656F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(2.7613F, -6.097F, 2.3817F, 0.3927F, 0.0F, 0.0F));

        PartDefinition cube_r26 = group.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(64, 60).addBox(-0.293F, -0.9746F, -1.2578F, 2.0078F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9301F, -8.3059F, 3.6094F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r27 = group.addOrReplaceChild("cube_r27", CubeListBuilder.create().texOffs(46, 43).addBox(-0.293F, -0.9746F, -1.2578F, 2.0938F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.8193F, -17.1485F, 3.6094F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r28 = group.addOrReplaceChild("cube_r28", CubeListBuilder.create().texOffs(17, 31).addBox(-0.293F, -0.9746F, -1.2578F, 2.0938F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.6787F, -18.7188F, 3.6094F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r29 = group.addOrReplaceChild("cube_r29", CubeListBuilder.create().texOffs(32, 6).mirror().addBox(-5.373F, -1.2148F, -1.7676F, 7.5156F, 2.0234F, 2.2422F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(3.8552F, -16.653F, 4.1191F, 0.0F, 0.0F, -0.3927F));

        PartDefinition cube_r30 = group.addOrReplaceChild("cube_r30", CubeListBuilder.create().texOffs(13, 55).addBox(-0.7559F, -1.7695F, -3.3848F, 1.4219F, 1.5352F, 1.2695F, new CubeDeformation(0.0F))
                .texOffs(49, 24).addBox(-3.0762F, -1.5156F, -3.166F, 6.1523F, 2.3242F, 5.9531F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.1581F, -14.1374F, 3.7246F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r31 = group.addOrReplaceChild("cube_r31", CubeListBuilder.create().texOffs(58, 62).addBox(-1.7148F, -0.9746F, -1.2578F, 2.0078F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(23.9434F, -8.3059F, 3.6094F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r32 = group.addOrReplaceChild("cube_r32", CubeListBuilder.create().texOffs(53, 62).addBox(-1.8008F, -0.9746F, -1.2578F, 2.0938F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(22.1939F, -17.1485F, 3.6094F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r33 = group.addOrReplaceChild("cube_r33", CubeListBuilder.create().texOffs(47, 62).mirror().addBox(-1.8008F, -0.9746F, -1.2578F, 2.0938F, 0.4336F, 2.2422F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(18.3345F, -18.7188F, 3.6094F, 0.0F, 0.0F, 0.3927F));

        PartDefinition cube_r34 = group.addOrReplaceChild("cube_r34", CubeListBuilder.create().texOffs(1, 17).addBox(-2.1426F, -1.2148F, -1.7676F, 7.5156F, 2.0234F, 2.2422F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(19.1581F, -16.653F, 4.1191F, 0.0F, 0.0F, 0.3927F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(FirstMonster entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        group2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        group.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}

